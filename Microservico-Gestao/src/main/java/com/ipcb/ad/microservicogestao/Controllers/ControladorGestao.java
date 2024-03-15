package com.ipcb.ad.microservicogestao.Controllers;

import com.ipcb.ad.microservicogestao.Configs.ByteArrayMultipartFile;
import com.ipcb.ad.microservicogestao.Models.PredictionDTO;
import com.ipcb.ad.microservicogestao.Models.TarefaDTO;
import com.ipcb.ad.microservicogestao.Proxy.ProxyMicroservicoIA;
import com.ipcb.ad.microservicogestao.Services.ServicoGestao;
import com.ipcb.ad.microservicogestao.Services.ServicoUtilizador;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class ControladorGestao {
    private final Path root = Paths.get("src/main/resources/static/uploads/");

    private final ServicoGestao servicoGestao;
    private final ServicoUtilizador servicoUtilizador;

    @Autowired
    private ProxyMicroservicoIA proxyMicroservicoIA;

    @Autowired
    private ControladorGestao(ServicoGestao servicoGestao, ServicoUtilizador servicoUtilizador){
        this.servicoGestao = servicoGestao;
        this.servicoUtilizador = servicoUtilizador;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @GetMapping("/users/{userId}/block")
    public String bloquearUtilizador(@PathVariable("userId") Integer userId, Model model, RedirectAttributes redirectAttributes) {
        int updateSuccess = servicoGestao.bloquearUtilizador(userId);

        if (updateSuccess > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "Utilizador bloqueado com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao bloquear o utilizador.");
        }
        model.addAttribute("utilizadores", servicoUtilizador.getUtilizadores());

        return "lista-utilizadores";
    }


    @GetMapping("/tarefas")
    public String listarTarefas(Model model, Principal principal) {
        Optional<List<TarefaDTO>> listaDeTarefas = servicoGestao.getTarefas(principal);
        model.addAttribute("listaDeTarefas", listaDeTarefas.orElse(Collections.emptyList()));

        return "lista-tarefas";
    }

    @GetMapping("/users")
    public String listarUtilizadores(Model model, Principal principal) {
        model.addAttribute("utilizadores", servicoUtilizador.getUtilizadores());
        model.addAttribute("selectedUsers", new ArrayList<Integer>());

        return "lista-utilizadores";
    }

    @PostMapping("/users")
    public String removerUtilizadores(@RequestParam(name = "selectedUsers", required = false) List<Integer> selectedUsers, Model model) {
        if (selectedUsers != null) {
            servicoUtilizador.removerUtilizadores(selectedUsers);
        }

        model.addAttribute("utilizadores", servicoUtilizador.getUtilizadores());

        return "lista-utilizadores";
    }
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestPart("imagem") MultipartFile file,
                             Model model,
                             RedirectAttributes attributes, Principal principal) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        servicoGestao.guardarImagem(file, this.root + "/" + fileName);

        attributes.addFlashAttribute("mensagem", "Realizou com sucesso o upload do ficheiro: " + fileName + '!');

        Integer userId = servicoUtilizador.getUtilizador(principal).getId();
        model.addAttribute("predictions", servicoGestao.getPredictions(file, userId));
        // Adiciona os resultados ao modelo
        model.addAttribute("resultText", "Upload realizado com sucesso: " + fileName);
        model.addAttribute("fileName", fileName);
        return "index";
    }

    @PostMapping("/uploadByUrl")
    public String uploadFileByUrl(@RequestParam("imageUrl") String imageUrl,
                                  Model model,
                                  RedirectAttributes attributes, Principal principal) {

        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        try {
            String fileExtension = FilenameUtils.getExtension(imageUrl);
//            fileName = fileName + "." + fileExtension;

            Path path = Paths.get(this.root + "/" + fileName);

            byte[] imageBytes = IOUtils.toByteArray(new URL(imageUrl).openStream());

            MultipartFile multipartFile = new ByteArrayMultipartFile("file", fileName, "image/" + fileExtension, imageBytes);

            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            TarefaDTO tarefa = proxyMicroservicoIA.postTarefa(multipartFile, servicoUtilizador.getUtilizador(principal).getId());
            List<PredictionDTO> predictions = tarefa.getObjetosIdentificados();

            model.addAttribute("predictions", predictions);

            // Adiciona os resultados ao modelo
            model.addAttribute("resultText", "Upload realizado com sucesso: " + fileName);
            model.addAttribute("imageUrl", imageUrl);

        } catch (IOException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("mensagem", "Erro ao baixar a imagem da URL.");
        }

        attributes.addFlashAttribute("mensagem", "Realizou com sucesso o upload do ficheiro: " + fileName + '!');
        return "index";
    }

}

package com.ipcb.ad.microservicoia.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipcb.ad.microservicoia.Models.Detector;
import com.ipcb.ad.microservicoia.Models.Enumerations.EstadoTarefa;
import com.ipcb.ad.microservicoia.Models.Prediction;
import com.ipcb.ad.microservicoia.Models.Tarefa;
import com.ipcb.ad.microservicoia.Repositories.RepositorioTarefa;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServicoTarefas {
    private final RepositorioTarefa repositorioTarefa;
    @Value("${codeprojectserver}")
    private String codeProjectServer;

    private ServicoTarefas(RepositorioTarefa repositorioTarefa){
        this.repositorioTarefa = repositorioTarefa;
    }

    public Optional<List<Tarefa>> obterTarefas(Integer userId){
        try {
            return repositorioTarefa.findAllByUserId(userId);

        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro a obter tarefas");
        }
    }

    public Tarefa obterTarefa(Integer userId, Long id){
        try{
            // Verificar se tarefa existe para o utilizador
            Optional<Tarefa> t = repositorioTarefa.findTarefaByIdAndUserId(id, userId);
            if(t.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não há essa tarefa para o utilizador.");
            }

            return t.get();

        } catch (ResponseStatusException e){
            throw e;

        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro a obter tarefa");
        }
    }

    private String calcHash(MultipartFile imagem){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            return DatatypeConverter.printHexBinary(md.digest(imagem.getResource().getContentAsByteArray()));

        } catch (NoSuchAlgorithmException | IOException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro a processar imagem");
        }
    }

    private Detector getDetector(MultipartFile imagem, Tarefa tarefa){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image1", imagem.getResource());

            // Encapsular informacao
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            System.out.println(codeProjectServer);
            String url = "http://"+codeProjectServer+"/v1/vision/custom/grocereye";
            ResponseEntity<String> resp =  new RestTemplate().postForEntity(url, requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(resp.getBody(), Detector.class);

        } catch (JsonProcessingException e){
            tarefa.setEstado(EstadoTarefa.CANCELADA);
            repositorioTarefa.save(tarefa);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao fazer deteção");
        }
    }

    public Tarefa criarTarefa(Integer userId, MultipartFile imagem){

        Tarefa tarefa = new Tarefa();

        try {
            tarefa.setUserId(userId);
            // Calcular hash da imagem
            tarefa.setHashImagem(calcHash(imagem));
            tarefa.setEstado(EstadoTarefa.EM_PROCESSAMENTO);
            tarefa.setNomeOuUrlImagem(imagem.getOriginalFilename());
            tarefa.setInicioTarefa(LocalDateTime.now());

            repositorioTarefa.save(tarefa);

            // Fazer pedido para detectar
            Detector detector = getDetector(imagem, tarefa);
            tarefa.setObjetosIdentificados(detector.getPredictions());

            for (Prediction prediction : detector.getPredictions()) {
                prediction.setTarefa(tarefa);
            }
            tarefa.setFimTarefa(LocalDateTime.now());
            tarefa.setDuracao(Duration.between(tarefa.getInicioTarefa(), tarefa.getFimTarefa()).toSeconds());
            tarefa.setEstado(EstadoTarefa.CONCLUIDA);

            repositorioTarefa.save(tarefa);

        } catch (Exception e){
            tarefa.setEstado(EstadoTarefa.CANCELADA);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao guardar deteção");
        }

        return tarefa;
    }
}

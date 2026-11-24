package sistema.adocao.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/** Guarda as fotos dos animais em disco (pasta definida em app.upload-dir). */
@Service
public class ArmazenamentoService {

    private final Path raiz;

    public ArmazenamentoService(@Value("${app.upload-dir:uploads}") String diretorio) throws IOException {
        this.raiz = Path.of(diretorio).toAbsolutePath().normalize();
        Files.createDirectories(raiz);
    }

    public Path getRaiz() { return raiz; }

    public String salvar(MultipartFile arquivo) throws IOException {
        String tipo = arquivo.getContentType() == null ? "" : arquivo.getContentType();
        String extensao = switch (tipo) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException("Envie uma foto em JPG, PNG ou WEBP.");
        };
        String nome = UUID.randomUUID() + extensao;
        try (InputStream in = arquivo.getInputStream()) {
            Files.copy(in, raiz.resolve(nome), StandardCopyOption.REPLACE_EXISTING);
        }
        return nome;
    }

    public void remover(String nome) {
        if (nome == null || nome.isBlank()) return;
        try {
            Path alvo = raiz.resolve(nome).normalize();
            if (alvo.startsWith(raiz)) Files.deleteIfExists(alvo);
        } catch (IOException ignorado) {
            // arquivo órfão não impede o fluxo
        }
    }
}

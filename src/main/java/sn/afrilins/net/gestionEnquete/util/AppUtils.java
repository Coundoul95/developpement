package sn.afrilins.net.gestionEnquete.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Classe utilitaire centralisée pour certaines fonctions personnalisées dans l'application.
 */
public class AppUtils {


    public static final Map<String, List<String>> FILE_EXTENSION_CATEGORY_MAP = Map.of(
            "document", List.of("pdf", "doc", "docx", "odt", "xls", "xlsx", "csv", "ppt", "pptx", "txt", "md"),
            "image", List.of("jpg", "jpeg", "png", "gif", "svg"),
            "audio", List.of("mp3", "wav", "flac"),
            "video", List.of("mp4", "avi", "mov", "wmv", "mkv"),
            "archive", List.of("zip", "rar", "7z"),
            "code", List.of("json", "xml", "html", "htm", "css", "js", "ts"),
            "executable", List.of("exe", "apk", "iso")
    );


    /**
     * Récupère l'extension d'un fichier à partir de son nom.
     *
     * @param filename Le nom du fichier (ex. "rapport.pdf").
     * @return L'extension du fichier (ex. "pdf") ou "inconnu" si non déterminée.
     */
    public static String getExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf('.') + 1);
        }
        return "inconnu";
    }

    /**
     * Méthode fictive pour sauvegarder un fichier. À implémenter selon les besoins du projet.
     *
     * @param file         Le fichier à sauvegarder.
     * @param documentName Le nom à attribuer au fichier.
     * @return Le chemin vers le fichier sauvegardé.
     */
    public static String saveFile(MultipartFile file, String documentName) {
        // À implémenter : logique réelle de sauvegarde du fichier sur disque ou cloud
        return "path/to/saved/file";
    }

    /**
     * Retourne le type MIME associé à une extension de fichier.
     * Utile pour définir le content-type lors du téléchargement ou de la prévisualisation.
     *
     * @param extension L'extension du fichier (ex. "pdf", "png").
     * @return Le type MIME correspondant (ex. "application/pdf") ou "application/octet-stream" si inconnu.
     */
    public static String getContentTypeByExtension(String extension) {
        if (extension == null) return "application/octet-stream";

        switch (extension.toLowerCase()) {
            case "pdf": return "application/pdf";
            case "doc": return "application/msword";
            case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls": return "application/vnd.ms-excel";
            case "xlsx": return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt": return "application/vnd.ms-powerpoint";
            case "pptx": return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "txt": return "text/plain";
            case "html":
            case "htm": return "text/html";
            case "xml": return "application/xml";
            case "svg": return "image/svg+xml";
            case "jpg":
            case "jpeg": return "image/jpeg";
            case "png": return "image/png";
            case "gif": return "image/gif";
            case "mp4": return "video/mp4";
            case "webm": return "video/webm";
            case "ogg": return "video/ogg";
            case "mp3": return "audio/mpeg";
            case "wav": return "audio/wav";
            default: return "application/octet-stream";
        }
    }

    /**
     * Vérifie si une extension de fichier est prévisualisable dans un navigateur
     * via une balise HTML comme {@code <embed>} ou {@code <video>} ou {@code <audio>}.
     *
     * @param extension L'extension du fichier.
     * @return {@code true} si le fichier peut être affiché dans le navigateur, sinon {@code false}.
     */
    public static boolean isPreviewable(String extension) {
        if (extension == null) return false;

        String[] previewableExtensions = {
                "pdf", "txt", "html", "htm", "xml", "svg",
                "jpg", "jpeg", "png", "gif",
                "mp4", "webm", "ogg", "mp3", "wav"
        };

        return Arrays.asList(previewableExtensions).contains(extension.toLowerCase());
    }
}

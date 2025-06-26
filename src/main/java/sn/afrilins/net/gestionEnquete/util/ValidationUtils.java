package sn.afrilins.net.gestionEnquete.util;

import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;

import java.util.Collection;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Classe utilitaire centralisée pour effectuer des validations personnalisées dans l'application.
 * Chaque méthode lève une {@link CustomBadRequestException} avec un {@link BadRequestAlertException}
 * spécifique en cas d'invalidité.
 */
public class ValidationUtils {

    /**
     * Vérifie qu'un objet n'est pas nul.
     *
     * @param value   l'objet à valider
     * @param field   le nom du champ concerné
     * @param entity  le nom de l'entité liée à la validation
     */
    public static void requireNonNull(Object value, String field, String entity) {
        if (value == null) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_requis", entity, field + "_null"));
        }
    }

    /**
     * Vérifie qu'une chaîne de caractères n'est pas vide ou composée uniquement d'espaces.
     *
     * @param value   la chaîne à valider
     * @param field   le nom du champ
     * @param entity  le nom de l'entité
     */
    public static void requireNonBlank(String value, String field, String entity) {
        if (value == null || value.trim().isEmpty()) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_requis", entity, field + "_vide"));
        }
    }

    /**
     * Vérifie qu'une chaîne respecte une taille minimale.
     *
     * @param value   la chaîne à valider
     * @param min     taille minimale autorisée
     * @param field   nom du champ
     * @param entity  entité liée
     */
    public static void requireMinLength(String value, int min, String field, String entity) {
        if (value == null || value.length() < min) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_trop_court", entity, field + "_court"));
        }
    }

    /**
     * Vérifie qu'une chaîne ne dépasse pas une taille maximale.
     *
     * @param value   la chaîne à valider
     * @param max     taille maximale autorisée
     * @param field   nom du champ
     * @param entity  entité liée
     */
    public static void requireMaxLength(String value, int max, String field, String entity) {
        if (value != null && value.length() > max) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_trop_long", entity, field + "_long"));
        }
    }

    /**
     * Vérifie qu'un identifiant est strictement positif.
     *
     * @param id      identifiant à valider
     * @param field   nom du champ
     * @param entity  entité liée
     */
    public static void requirePositiveId(Long id, String field, String entity) {
        if (id == null || id <= 0) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_invalide", entity, field + "_invalide"));
        }
    }

    /**
     * Vérifie qu'une adresse email est au format valide.
     *
     * @param email   adresse email à valider
     * @param field   nom du champ
     * @param entity  entité liée
     */
    public static void requireValidEmail(String email, String field, String entity) {
        if (email == null || !Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_email_invalide", entity, field + "_email"));
        }
    }

    /**
     * Vérifie qu'une chaîne est un UUID valide.
     *
     * @param uuid    chaîne à valider
     * @param field   nom du champ
     * @param entity  entité liée
     */
    public static void requireValidUUID(String uuid, String field, String entity) {
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_uuid_invalide", entity, field + "_uuid"));
        }
    }

    /**
     * Vérifie qu'une valeur entière se trouve dans une plage donnée.
     *
     * @param value   valeur à vérifier
     * @param min     valeur minimale
     * @param max     valeur maximale
     * @param field   nom du champ
     * @param entity  entité liée
     */
    public static void requireInRange(int value, int min, int max, String field, String entity) {
        if (value < min || value > max) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_hors_plage", entity, field + "_range"));
        }
    }

    /**
     * Vérifie qu'une liste n'est pas vide ou nulle.
     *
     * @param list    liste à valider
     * @param field   nom du champ
     * @param entity  entité liée
     */
    public static void requireNonEmptyList(Collection<?> list, String field, String entity) {
        if (list == null || list.isEmpty()) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_vide", entity, field + "_list_empty"));
        }
    }

    /**
     * Vérifie qu'un numéro de téléphone est au format international (ex : +221771234567).
     *
     * @param phone   numéro à valider
     * @param field   nom du champ
     * @param entity  entité liée
     */
    public static void requireValidPhone(String phone, String field, String entity) {
        if (phone == null || !Pattern.matches("^\\+?[1-9]\\d{7,14}$", phone)) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_telephone_invalide", entity, field + "_tel"));
        }
    }

    /**
     * Vérifie qu'une chaîne respecte un format défini par une expression régulière.
     *
     * @param value   valeur à valider
     * @param regex   expression régulière à respecter
     * @param field   nom du champ
     * @param entity  entité liée
     */
    public static void requirePatternMatch(String value, String regex, String field, String entity) {
        if (value == null || !Pattern.matches(regex, value)) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_format_invalide", entity, field + "_pattern"));
        }
    }

    /**
     * Vérifie qu'une valeur entière est strictement positive (> 0).
     *
     * @param value   la valeur à vérifier
     * @param field   nom du champ concerné
     * @param entity  entité liée à la validation
     */
    public static void requirePositive(int value, String field, String entity) {
        if (value <= 0) {
            throw new CustomBadRequestException(new BadRequestAlertException(field + "_doit_etre_positif", entity, field + "_negatif"));
        }
    }


}

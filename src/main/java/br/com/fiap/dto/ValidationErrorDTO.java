package br.com.fiap.dto;

import java.util.List;

public record ValidationErrorDTO (List<String> errors, int httpStatusCode){

}

package br.com.zup.edu.universidade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MatriculaAlunoException extends RuntimeException {
    public MatriculaAlunoException(String mensagem) {
    }
}

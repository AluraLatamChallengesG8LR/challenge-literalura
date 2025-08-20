package com.alura.literalura.service;

import com.alura.literalura.entity.AutorEntity;
import com.alura.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConsultaAutoresService {

    @Autowired
    private AutorRepository autorRepository;

    /**
     * Listar todos los autores registrados
     */
    public List<AutorEntity> listarTodosLosAutores() {
        System.out.println("👥 Obteniendo lista de todos los autores registrados...");
        return autorRepository.findAll();
    }

    /**
     * Listar autores vivos en un año específico
     */
    public List<AutorEntity> listarAutoresVivosEnAno(Integer ano) {
        System.out.println("📅 Buscando autores vivos en el año: " + ano);
        return autorRepository.findAutoresVivosEnAno(ano);
    }
}

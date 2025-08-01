package br.com.fiap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.dto.ItemMenuDTO;
import br.com.fiap.dto.MenuCreateDTO;
import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.interfaces.services.MenuService;
import br.com.fiap.interfaces.swagger.MenuApi;
import jakarta.validation.Valid;

@RestController
@RequestMapping("menus")
public class MenuController implements MenuApi{

    @Autowired
    private MenuService menuService;

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @PostMapping
    public ResponseEntity<MenuDTO> create(@RequestBody @Valid MenuCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.create(dto));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<MenuResponseDTO>> findAll() {
        return ResponseEntity.ok(menuService.findAll());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.findById(id));
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<MenuDTO> update(@PathVariable Long id, @RequestBody @Valid MenuCreateDTO dto) {
        return ResponseEntity.ok(menuService.update(id, dto));
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @PostMapping("/item")
    public ResponseEntity<Void> addItensMenu( @RequestBody @Valid ItemMenuDTO dto) {
    	menuService.addItemToMenu(dto);
    	return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

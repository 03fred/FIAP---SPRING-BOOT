package br.com.fiap.interfaces.services;

import java.util.List;

import br.com.fiap.dto.ItemMenuDTO;
import br.com.fiap.dto.MenuCreateDTO;
import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;

public interface MenuService {
    MenuDTO create(MenuCreateDTO dto);
    List<MenuResponseDTO> findAll();
    MenuResponseDTO findById(Long id);
    MenuDTO update(Long id, MenuCreateDTO dto);
    void delete(Long id);
    void addItemToMenu(ItemMenuDTO dto);
    void removeItemFromMenu(ItemMenuDTO dto);
}
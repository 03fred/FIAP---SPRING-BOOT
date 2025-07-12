package br.com.fiap.factory;

import br.com.fiap.dto.MenuDTO;
import br.com.fiap.dto.MenuResponseDTO;
import br.com.fiap.model.Menu;

public class MenuFactory {

    public static Menu createMenu(Long id) {
        Menu menu = new Menu();
        menu.setId(id);
        return menu;
    }

    public static MenuDTO createMenuDTO() {
        return new MenuDTO(
                "Hamburguer",
                "Lanche",
                "30",
                "11 am",
                "image/photo");
    }

    public static MenuResponseDTO createMenuResponseDTO() {
        return new MenuResponseDTO(
                "Hamburguer",
                "Lanche",
                "30",
                "11 am",
                "image/photo");
    }
}

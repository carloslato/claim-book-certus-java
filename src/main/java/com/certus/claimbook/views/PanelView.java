package com.certus.claimbook.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.certus.claimbook.entities.Reclamo;
import com.certus.claimbook.repositories.EstadoRepository;
import com.certus.claimbook.repositories.ReclamoRepository;
import com.certus.claimbook.repositories.TipoReclamoRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("panel/")
public class PanelView extends VerticalLayout {
	
	@Autowired
    private ReclamoRepository reclamoRepo;

    @Autowired
    private TipoReclamoRepository tipoRepo;
    
    @Autowired
    private EstadoRepository estadoRepo;

    public PanelView(ReclamoRepository reclamoRepo) {

        Grid<Reclamo> grid = new Grid<>(Reclamo.class);
        grid.setItems(reclamoRepo.findAll());

        grid.setColumns("id", "nombre", "descripcion");
        grid.addColumn(r -> r.getTipo().getDescripcion()).setHeader("Tipo");
        grid.addColumn(r -> r.getFechaCreacion().toString()).setHeader("Fecha");

        // link al detalle
        grid.addComponentColumn(r -> {
            return new Anchor("/panel/" + r.getId(), "Ver detalle");
        }).setHeader("Acciones");

        add(new H2("Listado de Reclamos"), grid);
    }
}

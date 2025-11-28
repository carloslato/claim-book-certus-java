package com.certus.claimbook.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("test/")
public class HomeView extends VerticalLayout {

    public HomeView() {

        add(new H1("Welcome to your new application, java"));
        add(new Paragraph("This is the home view"));

        add(new Paragraph("You can edit this view in src\\main\\java\\com\\certus\\claimbook\\views\\HomeView.java"));
        
        add(new Button("Make visible"));
        
        FormLayout form = new FormLayout();

        TextField name = new TextField("Name");
        TextField email = new TextField("E-mail");
        Button submit = new Button("Submit");

        form.add(name, email, submit);
        
        add(form);

    }
}

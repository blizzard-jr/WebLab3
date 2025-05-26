package org.example.weblab3;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Named("clockBean")
@ViewScoped
public class ClockBean implements Serializable {
    public String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(new Date());
    }
}

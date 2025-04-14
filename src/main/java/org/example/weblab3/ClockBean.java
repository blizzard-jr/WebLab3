package org.example.weblab3;

import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.ViewScoped;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean
@ViewScoped
public class ClockBean implements Serializable {
    public String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(new Date());
    }
}

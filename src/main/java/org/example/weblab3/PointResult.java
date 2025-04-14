package org.example.weblab3;




import jakarta.persistence.*;

@Entity
public class PointResult {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    public Double x;
    public Double y;
    public Double r;
    public Boolean result;
    public Long executionTime;
    public String checkTime;

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Проверяет, попадает ли точка в заданную область
     * @return true, если точка попадает в область, false - в противном случае
     */
    public boolean checkHit() {
        if (x == null || y == null || r == null) {
            return false;
        }
        
        // Проверка попадания в прямоугольник во втором квадранте (x ≤ 0, y ≥ 0)
        boolean inRectangle = x <= 0 && y >= 0 && Math.abs(x) <= r / 2 && y <= r;
        
        // Проверка попадания в треугольник в третьем квадранте (x ≤ 0, y ≤ 0)
        boolean inTriangle = x <= 0 && y <= 0 && y >= -x - r;
        
        // Проверка попадания в четверть круга в первом квадранте (x ≥ 0, y ≥ 0)
        boolean inCircle = x >= 0 && y >= 0 && x*x + y*y <= r*r;
        
        return inRectangle || inTriangle || inCircle;
    }

    // Геттеры и сеттеры
}

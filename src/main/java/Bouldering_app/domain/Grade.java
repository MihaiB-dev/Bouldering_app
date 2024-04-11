package Bouldering_app.domain;


public enum Grade {
    _4("green"), _5("green"), _5PLUS("green"),

    _6A("yellow"), _6APLUS("yellow"), _6B("yellow"), _6BPLUS("yellow"),
    _6C("yellow"), _6CPLUS("yellow"), _7A("yellow"),

    _7APLUS("red"), _7B("red"), _7BPLUS("red"), _7C("red"),

    _7CPLUS("purple"), _8A("purple"), _8APLUS("purple"), _8B("purple"),

    _8BPLUS("white"), _9A("white"), _8CPLUS("white");

    private String color;
    Grade(String color) {
        this.color = color;
    }

    public String getColor(){
        return color;
    }

    public double importance(){
        switch (this.color) {
            case "green" -> {return 0.1;}
            case "yellow" -> {return 0.2;}
            case "red" -> {return 0.35;}
            case "purple" -> {return 0.6;}
            case "white" -> {return 0.8;}
        }
        return 0;
    }
}


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

    public static Grade fromString(String grade){
        switch (grade){
            case "4" -> {return _4;}
            case "5" -> {return _5;}
            case "5+" -> {return _5PLUS;}
            case "6A" -> {return _6A;}
            case "6A+" -> {return _6APLUS;}
            case "6B" -> {return _6B;}
            case "6B+" -> {return _6BPLUS;}
            case "6C" -> {return _6C;}
            case "6C+" -> {return _6CPLUS;}
            case "7A" -> {return _7A;}
            case "7A+" -> {return _7APLUS;}
            case "7B" -> {return _7B;}
            case "7B+" -> {return _7BPLUS;}
            case "7C" -> {return _7C;}
            case "7C+" -> {return _7CPLUS;}
            case "8A" -> {return _8A;}
            case "8A+" -> {return _8APLUS;}
            case "8B" -> {return _8B;}
            case "8B+" -> {return _8BPLUS;}
            case "8C" -> {return _8CPLUS;}
            case "9A" -> {return _9A;}
        }
        return null;
    }
}


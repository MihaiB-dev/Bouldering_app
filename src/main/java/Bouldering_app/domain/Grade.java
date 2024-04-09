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
}


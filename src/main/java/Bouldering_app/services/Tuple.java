package Bouldering_app.services;

public class Tuple<A,B>{
    public A route; public B integer;

    public Tuple(A route, B integer) {
        this.route = route;
        this.integer = integer;
    }
    public A getValue1() {return route;}
    public B getValue2(){return integer;}
}

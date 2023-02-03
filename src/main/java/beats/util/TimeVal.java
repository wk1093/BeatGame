package beats.util;

public class TimeVal {
    private double ns;
    public TimeVal(double val, TimeType t) {
        ns = t.getNS(val);
    }

    public double get(TimeType t) {
        return t.get(ns);
    }
}

enum TimeType {
    NS(1, 1),
    NANOSECOND(1, 1),
    MS(1e-6, 1e+6),
    MILLISECOND(1e-6, 1e+6),
    S(1e-9, 1e+9),
    SEC(1e-9, 1e+9),
    SECOND(1e-9, 1e+9),
    MIN(1.6667e-11, 6e+10),
    MINUTE(1.6667e-11, 6e+10);

    private double ns_to, to_ns;

    private TimeType(double ns_to, double to_ns) { // ns to _, _ to ns
        this.ns_to = ns_to;
        this.to_ns = to_ns;
    }

    public double getNS(double val) {
        return val * to_ns;
    }

    public double get(double val) {
        return val * ns_to;
    }

}
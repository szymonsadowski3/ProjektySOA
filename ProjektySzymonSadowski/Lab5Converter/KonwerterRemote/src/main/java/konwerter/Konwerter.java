package konwerter;

import javax.ejb.Remote;

public interface Konwerter {

    double Fahr2Cels(double temp);

    double Cels2Fahr(double temp);
}

package konwerter;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(Konwerter.class)
public class KonwerterBean implements Konwerter {

    @Override
    public double Fahr2Cels(double temp) {
        return 5/9 * (temp - 32);
    }

    @Override
    public double Cels2Fahr(double temp) {
        return 9/5 * temp + 32;
    }
}

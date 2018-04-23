package konwerter;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("konwerterServlet")
public class KonwerterServlet extends HttpServlet {

    @EJB
    private Konwerter konwerterBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        double temperatura = Double.parseDouble(request.getParameter("temperatura"));
        String jednostka = request.getParameter("jednostka");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        switch (jednostka.toLowerCase()) {
            case "celcius": {
                out.println("Celcius: " + temperatura);
                out.println("Fahrenheit: " + konwerterBean.Cels2Fahr(temperatura));
                break;
            }
            case "fahrenheit": {
                out.println("Fahrenheit: " + temperatura);
                out.println("Celcius: " + konwerterBean.Fahr2Cels(temperatura));
                break;
            }
            default: {
                out.println("Błąd konwertera!");
                break;
            }
        }

        out.close();
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
    }
}

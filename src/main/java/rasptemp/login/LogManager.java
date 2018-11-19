package rasptemp.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.MeterGaugeChartModel;
import rasptemp.utils.EManager;

/**
 *
 * @author rodrigo/cnmoro
 */
@ManagedBean
@SessionScoped
public class LogManager implements Serializable {

    private MeterGaugeChartModel meterGaugeModel;
    private LineChartModel graficoLinha;
    private List<Log> logList = new ArrayList<>();
    private static int minValue = 0;
    private static int maxValue = 100;

    @PostConstruct
    public void init() {
        updateDbData();
        createLineModel();
        createGaugeModel();
    }

    public void updateDbData() {
        this.logList = EManager.getInstance().getLogAccessor().getLogs();
        //Mostra apenas os 10 últimos
        if (this.logList.size() > 10) {
            encolherLista(this.logList, 10);
        }

        //Nessa query, os logs mais recentes aparecem primeiro
        //Inverter para aparecem no final do grafico
        Collections.reverse(this.logList);

//        System.out.println("\n");
//        for (Log l : this.logList) {
//            System.out.println("log: " + l.getTemp() + ", " + l.getUmid() + ", " + l.getData());
//        }
        //adjustMinMaxValues();
        createLineModel();
        createGaugeModel();
    }

    public void encolherLista(List list, int novoTam) {
        int size = list.size();
        if (novoTam >= size) {
            return;
        }
        for (int i = novoTam; i < size; i++) {
            list.remove(list.size() - 1);
        }
    }

    //Muda o layout baseado nos valores do banco
    public void adjustMinMaxValues() {
        if (this.logList.size() > 0) {
            this.minValue = (int) this.logList.get(0).getTemp();
            this.maxValue = (int) this.logList.get(0).getTemp();

            for (Log l : this.logList) {
                //Min
                if (l.getTemp() <= minValue) {
                    minValue = (int) l.getTemp();
                }
                if (l.getUmid() <= minValue) {
                    minValue = (int) l.getTemp();
                }

                //Max
                if (l.getTemp() >= maxValue) {
                    maxValue = (int) l.getUmid();
                }
                if (l.getUmid() >= maxValue) {
                    maxValue = (int) l.getUmid();
                }
            }
        }
    }

    private void createGaugeModel() {
        meterGaugeModel = initMeterGaugeModel();
        meterGaugeModel.setTitle("Temperatura");
        meterGaugeModel.setGaugeLabel("ºC");
        meterGaugeModel.setGaugeLabelPosition("bottom");
        meterGaugeModel.setSeriesColors("66cc66,93b75f,E7E658,cc6666");
    }

    private MeterGaugeChartModel initMeterGaugeModel() {
        List<Number> intervals = new ArrayList<Number>() {
            {
                add(0);
                add(35);
                add(65);
                add(100);
            }
        };

        if (this.logList.size() > 1) {
            if ((int) this.logList.get(this.logList.size() - 1).getTemp() > 100) {
                return new MeterGaugeChartModel(100, intervals);
            } else {
                return new MeterGaugeChartModel((int) this.logList.get(this.logList.size() - 1).getTemp(), intervals);
            }
        } else {
            return new MeterGaugeChartModel(0, intervals);
        }
    }

    private void createLineModel() {
        graficoLinha = initLinearModel();
        graficoLinha.setTitle("Temperatura e Umidade");
        graficoLinha.setAnimate(false);
        graficoLinha.setLegendPosition("se");
        Axis yAxis = graficoLinha.getAxis(AxisType.Y);
        yAxis.setMin(minValue);
        yAxis.setMax(maxValue);
        yAxis.setLabel("Dados");
        graficoLinha.getAxes().put(AxisType.X, new CategoryAxis("Tempo"));
    }

    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();
        Calendar cal = Calendar.getInstance();

        ChartSeries temps = new ChartSeries();
        temps.setLabel("Temperatura");

        ChartSeries umids = new ChartSeries();
        umids.setLabel("Umidade");

        for (Log l : this.logList) {

            cal.setTime(l.getData());
            int hora = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            int seg = cal.get(Calendar.SECOND);
            String tempo;
            if (seg > 10) {
                tempo = hora + ":" + min + ":" + seg;
            } else {
                tempo = hora + ":" + min + ":" + seg + "0";
            }

            temps.set(tempo, l.getTemp());
            umids.set(tempo, l.getUmid());
        }

        model.addSeries(umids);
        model.addSeries(temps);
        
        return model;
    }

    public LineChartModel getGraficoLinha() {
        return graficoLinha;
    }

    public void setGraficoLinha(LineChartModel graficoLinha) {
        this.graficoLinha = graficoLinha;
    }

    public MeterGaugeChartModel getMeterGaugeModel() {
        return meterGaugeModel;
    }

    public void setMeterGaugeModel(MeterGaugeChartModel meterGaugeModel) {
        this.meterGaugeModel = meterGaugeModel;
    }

    public List<Log> getLogList() {
        return logList;
    }

    public void setLogList(List<Log> logList) {
        this.logList = logList;
    }

    public static int getMinValue() {
        return minValue;
    }

    public static void setMinValue(int minValue) {
        LogManager.minValue = minValue;
    }

    public static int getMaxValue() {
        return maxValue;
    }

    public static void setMaxValue(int maxValue) {
        LogManager.maxValue = maxValue;
    }
}

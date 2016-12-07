package converter.view;

import converter.controller.ConversionController;
import converter.model.ConversionDTO;

import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("converterManager")
@ConversationScoped
public class ConverterManager implements Serializable {
    @EJB
    private ConversionController conversionController;

    private double amountToConvert;
    private String currencyFrom;
    private String currencyTo;
    private ConversionDTO conversion;
    private Exception conversionFailure;
    @Inject
    private Conversation conversation;

    private void startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    private void stopConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }

    public ConversionDTO getConversion()
    {
        return conversion;
    }

    public double getAmountToConvert() {
        return amountToConvert;
    }

    public void setAmountToConvert(double amountToConvert) {
        this.amountToConvert = amountToConvert;
    }

    public String getCurrencyFrom()
    {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom)
    {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo()
    {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo)
    {
        this.currencyTo = currencyTo;
    }

    public String convert() {
        try {
            startConversation();
            conversionFailure = null;

            this.conversion = conversionController.findConversion(currencyFrom, currencyTo);
            System.out.println("Amount to convert: " + amountToConvert +
                "currency from: " + currencyFrom +
                "currency to: " + currencyTo);


        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    private void handleException(Exception e) {
        stopConversation();
        e.printStackTrace(System.err);
        conversionFailure = e;
    }

    /**
     * This return value is needed because of a JSF 2.2 bug. Note 3 on page 7-10
     * of the JSF 2.2 specification states that action handling methods may be
     * void. In JSF 2.2, however, a void action handling method plus an
     * if-element that evaluates to true in the faces-config navigation case
     * causes an exception.
     *
     * @return an empty string.
     */
    private String jsf22Bugfix() {
        return "";
    }

}

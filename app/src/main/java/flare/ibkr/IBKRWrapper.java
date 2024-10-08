package flare.ibkr;


import com.ib.client.*;
import flare.Analyst;
import flare.GenericBroker;
import flare.RequestStruct;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class IBKRWrapper implements EWrapper {

    private IBKRClient broker;
    private final Analyst analyst;
    private final int clientId;

    public IBKRWrapper(Analyst analyst, int instanceId) {
        this.analyst = analyst;
        this.clientId = instanceId;
    }

    public void setBroker(IBKRClient broker) {
        this.broker = broker;
    }

    @Override
    public void accountDownloadEnd(String s) {

    }

    @Override
    public void accountSummary(int i, String s, String s1, String s2, String s3) {

    }

    @Override
    public void accountSummaryEnd(int i) {

    }

    @Override
    public void accountUpdateMulti(int i, String s, String s1, String s2, String s3, String s4) {

    }

    @Override
    public void accountUpdateMultiEnd(int i) {

    }

    @Override
    public void bondContractDetails(int i, ContractDetails contractDetails) {

    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {

    }

    @Override
    public void completedOrder(Contract contract, Order order, OrderState orderState) {

    }

    @Override
    public void completedOrdersEnd() {

    }

    @Override
    public void connectAck() {
        System.out.printf("Client " + clientId + " connected to TWS.\n", clientId);
    }

    @Override
    public void connectionClosed() {
        System.out.printf("Client " + clientId + " disconnected from TWS.\n", clientId);
    }

    @Override
    public void contractDetails(int i, ContractDetails contractDetails) {

    }

    @Override
    public void contractDetailsEnd(int i) {

    }

    @Override
    public void currentTime(long l) {

    }

    @Override
    public void deltaNeutralValidation(int i, DeltaNeutralContract deltaNeutralContract) {

    }

    @Override
    public void displayGroupList(int i, String s) {

    }

    @Override
    public void displayGroupUpdated(int i, String s) {

    }

    @Override
    public void error(Exception e) {
        System.out.println("Client " + clientId + " Error: " + e.getMessage());
    }

    @Override
    public void error(String str) {
        System.out.println("Client " + clientId + " Error: " + str);
    }

    @Override
    public void error(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
        String formattedMessage = String.format("Client %d: IBKR TWS System [%d]: %s", clientId, errorCode, errorMsg);
        System.out.println(formattedMessage);
    }

    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
        broker.onOrderPlaced(execution.orderId(), execution.price(), execution.shares().longValue());
    }

    @Override
    public void execDetailsEnd(int i) {

    }

    @Override
    public void familyCodes(FamilyCode[] familyCodes) {

    }

    @Override
    public void fundamentalData(int i, String s) {

    }

    @Override
    public void headTimestamp(int i, String s) {

    }

    @Override
    public void histogramData(int i, List<HistogramEntry> list) {

    }

    @Override
    public void historicalData(int i, Bar bar) {

    }

    @Override
    public void historicalDataEnd(int i, String s, String s1) {

    }

    @Override
    public void historicalDataUpdate(int i, Bar bar) {

    }

    @Override
    public void historicalNews(int i, String s, String s1, String s2, String s3) {

    }

    @Override
    public void historicalNewsEnd(int i, boolean b) {

    }

    @Override
    public void historicalSchedule(int i, String s, String s1, String s2, List<HistoricalSession> list) {

    }

    @Override
    public void historicalTicks(int i, List<HistoricalTick> list, boolean b) {

    }

    @Override
    public void historicalTicksBidAsk(int i, List<HistoricalTickBidAsk> list, boolean b) {

    }

    @Override
    public void historicalTicksLast(int i, List<HistoricalTickLast> list, boolean b) {

    }

    @Override
    public void managedAccounts(String s) {

    }

    @Override
    public void marketDataType(int i, int i1) {

    }

    @Override
    public void marketRule(int i, PriceIncrement[] priceIncrements) {

    }

    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {

    }

    @Override
    public void newsArticle(int i, int i1, String s) {

    }

    @Override
    public void newsProviders(NewsProvider[] newsProviders) {

    }

    @Override
    public void nextValidId(int i) {

    }

    @Override
    public void openOrder(int i, Contract contract, Order order, OrderState orderState) {

    }

    @Override
    public void openOrderEnd() {

    }

    @Override
    public void orderBound(long l, int i, int i1) {

    }

    @Override
    public void orderStatus(int orderId, String status, Decimal filled, Decimal remaining, double avgFillPrice,
                            int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        System.out.printf("Client %d | Order ID: %d Status: %s Filled: %s Remaining: %s Average Fill Price: %.2f " +
                        "Perm ID: %d Parent ID: %d Last Fill Price: %.2f Client ID: %d Why Held: %s Market Cap Price: %.2f\n",
                clientId, orderId, status, filled, remaining, avgFillPrice,
                permId, parentId, lastFillPrice, clientId, whyHeld, mktCapPrice);
        if (status.equals("Filled")) {
            broker.onOrderFilled(orderId, avgFillPrice, filled.longValue());
        } else if (status.equals("Cancelled")) {
            broker.onOrderCancelled(orderId);
        }
    }

    @Override
    public void pnl(int i, double v, double v1, double v2) {

    }

    @Override
    public void pnlSingle(int i, Decimal decimal, double v, double v1, double v2, double v3) {

    }

    @Override
    public void position(String s, Contract contract, Decimal decimal, double v) {

    }

    @Override
    public void positionEnd() {

    }

    @Override
    public void positionMulti(int i, String s, String s1, Contract contract, Decimal decimal, double v) {

    }

    @Override
    public void positionMultiEnd(int i) {

    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, Decimal volume, Decimal wap, int count) {
        RequestStruct data = broker.getRequestData(reqId);
        analyst.listenBar(data, time, open, high, low, close, volume);
    }

    @Override
    public void receiveFA(int i, String s) {

    }

    @Override
    public void replaceFAEnd(int i, String s) {

    }

    @Override
    public void rerouteMktDataReq(int i, int i1, String s) {

    }

    @Override
    public void rerouteMktDepthReq(int i, int i1, String s) {

    }

    @Override
    public void scannerData(int i, int i1, ContractDetails contractDetails, String s, String s1, String s2, String s3) {

    }

    @Override
    public void scannerDataEnd(int i) {

    }

    @Override
    public void scannerParameters(String s) {

    }

    @Override
    public void securityDefinitionOptionalParameter(int i, String s, int i1, String s1, String s2, Set<String> set, Set<Double> set1) {

    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int i) {

    }

    @Override
    public void smartComponents(int i, Map<Integer, Map.Entry<String, Character>> map) {

    }

    @Override
    public void softDollarTiers(int i, SoftDollarTier[] softDollarTiers) {

    }

    @Override
    public void symbolSamples(int i, ContractDescription[] contractDescriptions) {

    }

    @Override
    public void tickByTickAllLast(int i, int i1, long l, double v, Decimal decimal, TickAttribLast tickAttribLast, String s, String s1) {

    }

    @Override
    public void tickByTickBidAsk(int i, long l, double v, double v1, Decimal decimal, Decimal decimal1, TickAttribBidAsk tickAttribBidAsk) {

    }

    @Override
    public void tickByTickMidPoint(int i, long l, double v) {

    }

    @Override
    public void tickEFP(int i, int i1, double v, String s, double v1, int i2, String s1, double v2, double v3) {

    }

    @Override
    public void tickGeneric(int i, int i1, double v) {

    }

    @Override
    public void tickNews(int i, long l, String s, String s1, String s2, String s3) {

    }

    @Override
    public void tickOptionComputation(int tickerId, int field, int tickAttrib, double impliedVol, double delta, double optPrice,
                                      double pvDividend, double gamma, double vega, double theta, double undPrice) {
        RequestStruct data = broker.getRequestData(tickerId);
        analyst.analyseOption(data.getSymbol(), "STK", undPrice, data.getStrike(), impliedVol, data.getLastTradeDate(), data.getRight(), optPrice);
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attribs) {
        // System.out.println("Tick Price: " + EWrapperMsgGenerator.tickPrice( tickerId, field, price, attribs));
        if (field == 2) {
            System.out.println("Setting 10Y tbill rate...");
            analyst.getModel().setRiskFree(price / 100);
        }
    }

    @Override
    public void tickReqParams(int i, double v, String s, int i1) {

    }

    @Override
    public void tickSize(int i, int i1, Decimal decimal) {

    }

    @Override
    public void tickSnapshotEnd(int i) {

    }

    @Override
    public void tickString(int i, int i1, String s) {

    }

    @Override
    public void updateAccountTime(String s) {

    }

    @Override
    public void updateAccountValue(String s, String s1, String s2, String s3) {

    }

    @Override
    public void updateMktDepth(int i, int i1, int i2, int i3, double v, Decimal decimal) {

    }

    @Override
    public void updateMktDepthL2(int i, int i1, String s, int i2, int i3, double v, Decimal decimal, boolean b) {

    }

    @Override
    public void updateNewsBulletin(int i, int i1, String s, String s1) {

    }

    @Override
    public void updatePortfolio(Contract contract, Decimal decimal, double v, double v1, double v2, double v3, double v4, String s) {

    }

    @Override
    public void userInfo(int i, String s) {

    }

    @Override
    public void verifyAndAuthCompleted(boolean b, String s) {

    }

    @Override
    public void verifyAndAuthMessageAPI(String s, String s1) {

    }

    @Override
    public void verifyCompleted(boolean b, String s) {

    }

    @Override
    public void verifyMessageAPI(String s) {

    }

    @Override
    public void wshEventData(int i, String s) {

    }

    @Override
    public void wshMetaData(int i, String s) {

    }
}

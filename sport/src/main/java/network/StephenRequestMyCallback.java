package network;


public abstract class StephenRequestMyCallback implements StephenRequestAsyncTask.RequestCallback {
    @Override
    public void onRequestPrepare() {}

    @Override
    public void onChangeProgress(int progress, int successFlag) {}

    @Override
    public void onCompleted(String returnMsg) {}

    @Override
    public boolean onCancel() {
        return false;
    }
}

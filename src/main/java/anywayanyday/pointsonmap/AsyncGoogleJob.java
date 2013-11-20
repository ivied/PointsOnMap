package anywayanyday.pointsonmap;


public class AsyncGoogleJob extends AsyncDataDownload{

    @Override
    public void dataDownload(DataRequest request, DownloaderListener downloaderListener) {
        this.downloaderListener = downloaderListener;
        if (!isNetworkOnline()) {
            downloaderListener.sendToast(downloaderListener.getContext().getResources().getString(R.id.toast_offline));
            return;
        }
    /*    switch (request.getRequestType()) {
            case DataRequest.GEO_DATA:
                downloadPoint(request);
                break;
            case DataRequest.MAP_TO_IMAGE_VIEW:
                new DownloadImage(request.getImageView()).execute(request.getUrl());
                break;

        }*/

    }
}

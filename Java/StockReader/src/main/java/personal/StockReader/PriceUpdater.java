package personal.StockReader;

public class PriceUpdater extends Thread {
	private Controller controller;

	public PriceUpdater(Controller controller) {
		super();
		this.controller = controller;
		this.setDaemon(true);
	}

	public void run() {
		while (true) {
			controller.getLockObj().lock();

			try {
				controller.read();

			} finally {
				controller.getLockObj().unlock();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

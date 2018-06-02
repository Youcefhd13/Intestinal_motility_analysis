/*
 */

package com.mycompany.imagej;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.*;
import ij.io.OpenDialog;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.plugin.frame.RoiManager;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import sc.fiji.multiKymograph.MultipleKymograph_;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

/**

 *
 * @author Youcef Kazwiny, KU Leuven 2018
 */
	public class Intestinal_motility_analysis extends JFrame implements ActionListener, KeyListener,MouseListener,PlugIn {
		protected ImagePlus image;
		public ImagePlus imp_scaled;
		public ImageProcessor ip_scaled;
		protected ImagePlus imp_big;
		protected ImagePlus imp_org;




	public MultipleKymograph_ Kymo = new MultipleKymograph_();

	double[] getKymo(ImagePlus imp, ImageProcessor ip, Roi roi, int linewidth, int proflength) {
		return Kymo.sKymo(imp, ip, roi, linewidth, proflength) ;
	}

	// image property members
	private int width;
	private int height;




	// plugin parameters
	public double value;





	public void run(java.lang.String arg)  {
		// get width and height

		doDialog();

	}

	/*
//-------------------------------------------------------------------------------
//-------------------------------------------------------------------------------
// Build the dialog box.
//-------------------------------------------------------------------------------
//-------------------------------------------------------------------------------
 */
	private BoxLayout mainLayout;
	static ImagePlus i1;
	static ImageCanvas ic;
	static int selected_cell;					// id of the selected balloon candidate to modification

	private GridBagLayout ActionLayout;
	private GridBagConstraints 	constraint;
	// Layout for Sub components of the Action panel
	private GridLayout 	boundsLayout;
	private GridLayout 	centerLayout;

	private JButton Ckymo;
	private JButton Skymo;
	private JButton updtkymo;
	private JCheckBox use_pixels;
	private JFormattedTextField magscalfield;
	private JLabel magscalfieldlabel;
	private JFormattedTextField timescalfield;
	private JLabel timescalfieldlabel;

	private JButton showresults;
	private JToggleButton Cntrselection;
	private JTextField	txtRem;
	private JTextField	txtCellID;
	private PolygonRoi ROIkym;


	private JFormattedTextField Lwidthfield;
	private JLabel Lwidthfieldlabel;

	private JFormattedTextField widthfield;
	private JLabel widthfieldlabel;





	RoiManager roiMng = new RoiManager();

	private void doDialog() {

		//limit to integer
		NumberFormat longFormat = NumberFormat.getIntegerInstance();

		NumberFormatter numberFormatter = new NumberFormatter(longFormat);
		numberFormatter.setValueClass(Long.class); //optional, ensures you will always get a long value
		numberFormatter.setAllowsInvalid(false); //this is the key!!
		numberFormatter.setMinimum(0l); //Optional


		// Layout
		ActionLayout = new GridBagLayout();
		constraint = new GridBagConstraints();

		boundsLayout = new GridLayout(5,2,6,10);
		centerLayout = new GridLayout(5,2,6,10);



		// buttons
		Ckymo = new JButton(" Create Kymograph ");
		Ckymo.setToolTipText("Create a Kymograph based on line selected");
		Ckymo.setBackground(new Color(255,200,200));
		Skymo = new JButton(" Apply Kymograph polyline");
		Skymo.setToolTipText("Uses the line selected in ROI manager as a basis for a Kymograph");
		Skymo.setBackground(new Color(255,200,200));
		updtkymo = new JButton(" Update Kymograph");
		updtkymo.setToolTipText("Update the Kymograph image with new parameters");
		updtkymo.setBackground(new Color(255,200,200));


		showresults = new JButton("Show Results");
		showresults.setBackground(Color.LIGHT_GRAY);
		use_pixels = new JCheckBox("No scale used(calculate using pixels)");
		magscalfield = new JFormattedTextField(numberFormatter);
		magscalfieldlabel = new JLabel("Image Scale(Optional)(px/mm)");

		timescalfield = new JFormattedTextField(numberFormatter);
		timescalfieldlabel = new JLabel("Frames per second (Optional)");


		Cntrselection = new JToggleButton(" Select Contractions ");
		Cntrselection.setToolTipText("Select Contractions on the Kymograph and save them in ROI manager by pressing 't'");
		Cntrselection.setBackground(Color.LIGHT_GRAY);





		txtRem = new JTextField("0", 1);
		txtCellID = new JTextField("0", 1);

		Lwidthfield = new JFormattedTextField(numberFormatter);
		Lwidthfield.setToolTipText("number of  pixels averaged around the drawn line");

		Lwidthfieldlabel = new JLabel("Polyline Width (odd number)");
		widthfield = new JFormattedTextField(numberFormatter);
		widthfield.setToolTipText("Size of Kymograph image in y direction (time)");
		widthfieldlabel = new JLabel("# time points in Kymograph");




		//Box for  Option/////////////////////////////////////////////////////////////////////

		// Panel parameters
		JPanel pnMain = new JPanel();
		mainLayout = new BoxLayout(pnMain, BoxLayout.X_AXIS);
		JPanel pnAction = new JPanel();

		JPanel pnKymo = new JPanel();
		Border bd = BorderFactory.createBevelBorder(BevelBorder.RAISED); //BorderFactory.createEtchedBorder(); //
		pnKymo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(bd, "Kymograph"), BorderFactory.createEmptyBorder(3, 3, 7, 7)));
		JPanel pnROIstats = new JPanel();
		pnROIstats.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(bd, "Contraction profiles"), BorderFactory.createEmptyBorder(3, 3, 7, 7)));

		JPanel pnButtons = new JPanel();


		// Panel for the slider

		pnMain.setLayout(mainLayout);
		pnAction.setLayout(ActionLayout);


		// Add components
		//addComponent(pnAction, 30, 0, 1, 1, 2, new JLabel("BOUNDS"));
		pnKymo.add(Lwidthfieldlabel);
		pnKymo.add(Lwidthfield);
		pnKymo.add(widthfieldlabel);
		pnKymo.add(widthfield);
		pnKymo.add(Skymo);
		pnKymo.add(Ckymo);
		pnKymo.add(updtkymo);

		pnKymo.setLayout(boundsLayout);

		//addComponent(pnCenters, 55, 0, 1, 1, 5, new JLabel("CENTERS"));
		pnROIstats.add(Cntrselection);
		pnROIstats.add(showresults);
		pnROIstats.add(use_pixels);
		pnROIstats.add(Box.createRigidArea(new Dimension(5,0)));

		pnROIstats.add(magscalfieldlabel);
		pnROIstats.add(magscalfield);
		pnROIstats.add(timescalfieldlabel);
		pnROIstats.add(timescalfield);

		pnROIstats.add(new Label(""));
		pnROIstats.setLayout(centerLayout);

		// Implement the listeners
		// close listener
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				((JFrame)e.getSource()).setState(JFrame.ICONIFIED);
			}
		});

		// buttons listeners
		Ckymo.addActionListener(this);
		Skymo.addActionListener(this);
		updtkymo.addActionListener(this);
		showresults.addActionListener(this);
		Cntrselection.addActionListener(this);
		use_pixels.addActionListener(this);

		if (WindowManager.getCurrentImage()==null)
		{
			OpenDialog od = new OpenDialog("Open image ...");
			String fileName = od.getPath();
			if (fileName == null) return;
			i1 = IJ.openImage(fileName);
			i1.setTitle("image");
			i1.show();


			roiMng.setVisible(true);
			roiMng.runCommand("Show All");
			IJ.setTool("polyline");

			}

		else i1 = WindowManager.getCurrentImage();

		ic = i1.getCanvas();

		ic.addKeyListener(this);
		ic.addMouseListener(this);

		////////////////////////////////////////////////////////////////
		// Build panel
		////////////////////////////////////////////////////////////////
		addComponent(pnAction, 1, 0, 1, 1, 5, pnKymo);
		addComponent(pnAction, 2, 0, 1, 1, 5, pnROIstats);
		addComponent(pnAction, 1, 0, 1, 1, 5, pnButtons);

		pnMain.add(pnAction);


		add(pnMain);
		pack();
		setResizable(true);
		GUI.center(this);
		setVisible(true);
		Point IJ_location = (IJ.getInstance()).getLocation();
		int Dialog_width = getWidth();
		int XX = (int)(IJ_location.getX()  + (IJ.getInstance()).getWidth()- Dialog_width) ;
		int YY = (int)(IJ_location.getY()  + (IJ.getInstance()).getHeight());
		Point Dialog_pos = new Point(XX, YY);
		setLocation(Dialog_pos);
	}

	final private void addComponent(
			final JPanel pn,
			final int row, final int col,
			final int width, final int height,
			final int space,
			final Component comp) {
		constraint.gridx = col;
		constraint.gridy = row;
		constraint.gridwidth = width;
		constraint.gridheight = height;
		constraint.anchor = GridBagConstraints.NORTHWEST;
		constraint.insets = new Insets(space, space, space, space);
		constraint.weightx = IJ.isMacintosh()?90:100;
		constraint.fill = constraint.HORIZONTAL;
		ActionLayout.setConstraints(comp, constraint);
		pn.add(comp);
	}

	public synchronized void adjustmentValueChanged(AdjustmentEvent e) {
		// check is slice number has changed

		notify();
	}
	public synchronized  void actionPerformed(ActionEvent e) {

		Toolbar Tb = Toolbar.getInstance();
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//
		//		B O U N D A R I E S
		//
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		if (e.getSource() == Skymo) {

			IJ.setTool("polyline");
			Roi[] KymROI = getRois();

			try
			{
				ROIkym = (PolygonRoi) KymROI[0];
			}
			catch( ClassCastException a )
			{
				JOptionPane.showMessageDialog(new JFrame(), "Please use a 'segmented line' selection, 'straight line' doesn't work ", "Error",
						JOptionPane.ERROR_MESSAGE);
			}


			ROIkym.setStrokeWidth(Float.valueOf(Lwidthfield.getText()));
		}
		if (e.getSource() == Ckymo) {
			ImagePlus impp = WindowManager.getCurrentImage();
			imp_org=impp;
			ImageProcessor iip = impp.getProcessor();
			double[] kyminst = this.getIrregularProfile(ROIkym, iip, 0);
			int proflength = kyminst.length;
			int thickness = Integer.valueOf(Lwidthfield.getText()).intValue();

			double[] average = getKymo(impp, iip, ROIkym, thickness ,proflength);
			FloatProcessor nip = new FloatProcessor(proflength, impp.getStackSize(), average);
			ImagePlus kymoimp = new ImagePlus("Kymograph", nip);
			kymoimp.show();

			imp_big = WindowManager.getCurrentImage();
			imp_scaled = imp_big.duplicate();
			ip_scaled = imp_scaled.getProcessor();
			imp_scaled.setTitle("scaled");
			//imp_scaled.show();

		}


		if (e.getSource() == updtkymo) {


			if (Lwidthfield.getText().isEmpty() || widthfield.getText().isEmpty()  ){
				JOptionPane.showMessageDialog(new JFrame(), "To update the kymograph,add kymograph width and desired # of time points in kymograph ", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			ImageProcessor iip = imp_org.getProcessor();

			double[] kyminst = this.getIrregularProfile(ROIkym, iip, 0);
			int proflength = kyminst.length;

			int thickness = Integer.valueOf(Lwidthfield.getText()).intValue();

			double[] average = getKymo(imp_org, iip, ROIkym, thickness ,proflength);
			FloatProcessor nip = new FloatProcessor(proflength, imp_org.getStackSize(), average);
			ImagePlus kymoimp = new ImagePlus("Kymograph", nip);
			int	kymolength = (int) Float.valueOf(widthfield.getText()).floatValue();






			int  kymowitdh =  nip.getWidth();
			nip = (FloatProcessor) nip.resize(kymowitdh,kymolength);
			kymoimp.setProcessor(nip);
			kymoimp.show();
			kymoimp.updateAndRepaintWindow();

			Roi[] KymROI = getRois();

			PolygonRoi ROIkym = (PolygonRoi) KymROI[0];
//
//
//
//
			ROIkym.setStrokeWidth(Float.valueOf(Lwidthfield.getText()).floatValue());

//

		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//
		//		Contraction results
		//
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////


		if (e.getSource() == Cntrselection) {

			RoiManager.getInstance().runCommand("delete");

			roiMng.setVisible(true);
			roiMng.runCommand("Show All");
			IJ.setTool("line");

		}

		if (e.getSource() == showresults) {

			Roi[] rois = getRois();
			double[] Ypositions = new double[rois.length];
			double[] interval = new double[rois.length];
			double[] ROIduration = new double[rois.length];
			double[] angle = new double[rois.length];
			double[] velocity = new double[rois.length];
			double[] distance = new double[rois.length];
			interval[0]=0;
			float magscale;
			float timescale;
			if (use_pixels.isSelected()){
					  magscale = 1;
					  timescale = 1;
				magscalfield.setOpaque(true);
				magscalfield.setDisabledTextColor(Color.red);
				repaint();

			}

			else {
				if (magscalfield.getText().isEmpty() || timescalfield.getText().isEmpty()  ){
					JOptionPane.showMessageDialog(new JFrame(), "please add scales or check 'no scales' box ", "Error",
							JOptionPane.ERROR_MESSAGE);
					}

				magscale = Float.valueOf(magscalfield.getText()).floatValue();
				 timescale = 1 /Float.valueOf(timescalfield.getText()).floatValue();
				}


			ResultsTable resulty = new ResultsTable();

			for (int i = 0; i < rois.length; i++) {

				Roi ROId = rois[i];
				i1.setRoi(rois[i]);
				angle[i] = ROId.getAngle();
				Line line = ( Line ) ROId;
				//slope of line drawn, positive means it is antegrade, negative is retrograde
				if ((line.y2-line.y1)/(line.x2-line.x1)>0){

					Ypositions[i] = Math.min(line.y1,line.y2)*timescale;

				}
				else {Ypositions[i] = Math.max(line.y1,line.y2)*timescale;}

				if (i > 0) {
					interval[i]= Ypositions[i]-Ypositions[i-1];
				}
				distance[i]= Math.abs(line.x1-line.x2)/magscale;
				ROIduration[i] = Math.abs(line.y1-line.y2)*timescale;  //  possibly add scale
				velocity[i] = distance[i]/ROIduration[i];


				resulty.incrementCounter();

				if (use_pixels.isSelected()) {
					resulty.addValue("Duration (p", (double) ROIduration[i]);
					resulty.addValue("velocity (p/p)", (double) velocity[i]);
					resulty.addValue("distance (p)", (double) distance[i]);
					resulty.addValue("Interval (p)", (double) interval[i]);

				}
				else	{
					resulty.addValue("Duration (s)", (double) ROIduration[i]);
					resulty.addValue("velocity (mm/s)", (double) velocity[i]);
					resulty.addValue("distance (mm)", (double) distance[i]);
					resulty.addValue("Interval (s)", (double) interval[i]);

				}

				resulty.show("results");

			}

			if (e.getSource() == use_pixels) {}



			notify();
		}
	}




			// CHECK THAT EDIT NOT SELECTED





	/**
	 * Main method for debugging.

	 * @param args unused
	 */
	public static void main(String[] args) {
		// set the plugins.dir property to make the plugin appear in the Plugins menu
		Class<?> clazz = Intestinal_motility_analysis.class;
		String url = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class").toString();
		String pluginsDir = url.substring("file:".length(), url.length() - clazz.getName().length() - ".class".length());
		System.setProperty("plugins.dir", pluginsDir);


		// start ImageJ
		final ImageJ ij = new ImageJ();

		// run the plugin
		IJ.runPlugIn(clazz.getName(), "");
		IJ.setTool("polyline");

	}




	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//		C L E A R
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////


	public synchronized void itemStateChanged(ItemEvent e) { notify(); 	}
	public void windowActivated(WindowEvent e) {}
	public void windowClosing(WindowEvent e) { dispose(); }
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) { 	}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}
	public Roi[] getRois() {
		RoiManager rmanager = RoiManager.getInstance();
		if (rmanager == null || rmanager.getCount() == 0) {
			IJ.log("add ROIs to the RoiManager first (select a region then press [t]).");
			return null;
		}
		return rmanager.getRoisAsArray();
	}


	// *************************
	// Mouse event manager
	// *************************
	public void mouseWheelMoved(MouseWheelEvent event) {
		//synchronized(this) {
	}
	public void mouseReleased(MouseEvent e)
	{
		Roi roi = i1.getRoi();
		if (roi !=null)
		{
			if (roi.getType() == roi.POLYGON & selected_cell>-1) // modify the selected cell
			{
				Polygon p = roi.getPolygon();
				int[] XXi = p.xpoints;
				int[] YYi = p.ypoints;



			}

		}
	}

	public void mousePressed(MouseEvent e)  	{
		if (Cntrselection.isSelected()){
			int x = ic.offScreenX(e.getX());
			int y = ic.offScreenY(e.getY());
			int xx=0;
		}

	}
	public void mouseExited(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}


	// *************************
	// KeyPress event manager
	// *************************
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e)
	{
		Roi roi = i1.getRoi();
		Polygon p = roi.getPolygon();
		int[] XXi = p.xpoints;
		int[] YYi = p.ypoints;

		PolygonRoi Proi = new PolygonRoi(XXi,YYi,(XXi).length,Roi.POLYGON);
		i1.setRoi(Proi);
	}

	// from  MultipleKymograph plugin bundled with Imagej
	double[] getIrregularProfile(Roi roi, ImageProcessor ip, int shift) {
		int n = ((PolygonRoi)roi).getNCoordinates();
		int[] x = ((PolygonRoi)roi).getXCoordinates();
		int[] y = ((PolygonRoi)roi).getYCoordinates();

		for(int r = 0; r < n; ++r) {
			x[r] += shift;
			y[r] += shift;
		}

		Rectangle var42 = roi.getBoundingRect();
		int xbase = var42.x;
		int ybase = var42.y;
		double length = 0.0D;
		double[] segmentLengths = new double[n];
		int[] dx = new int[n];
		int[] dy = new int[n];

		for(int values = 0; values < n - 1; ++values) {
			int xdelta = x[values + 1] - x[values];
			int ydelta = y[values + 1] - y[values];
			double segmentLength = Math.sqrt((double)(xdelta * xdelta + ydelta * ydelta));
			length += segmentLength;
			segmentLengths[values] = segmentLength;
			dx[values] = xdelta;
			dy[values] = ydelta;
		}

		double[] var43 = new double[(int)length];
		double leftOver = 1.0D;
		double distance = 0.0D;

		for(int i = 0; i < n; ++i) {
			double len = segmentLengths[i];
			if(len != 0.0D) {
				double xinc = (double)dx[i] / len;
				double yinc = (double)dy[i] / len;
				double start = 1.0D - leftOver;
				double rx = (double)(xbase + x[i]) + start * xinc;
				double ry = (double)(ybase + y[i]) + start * yinc;
				double len2 = len - start;
				int n2 = (int)len2;

				for(int j = 0; j <= n2; ++j) {
					int index = (int)distance + j;
					if(index < var43.length) {
						var43[index] = ip.getInterpolatedValue(rx, ry);
					}

					rx += xinc;
					ry += yinc;
				}

				distance += len;
				leftOver = len2 - (double)n2;
			}
		}

		return var43;
	}


}

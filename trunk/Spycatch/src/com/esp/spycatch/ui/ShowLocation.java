package com.esp.spycatch.ui;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;

import com.esp.spycatch.R;
import com.esp.spycatch.uc.PageTitle;
import com.esp.spycatch.util.Const;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ShowLocation extends MapActivity{
	
	private MapView mapView;
	private MapController mapController;
	public PageTitle pageTitle;
	public String Lat,Long;
	 @Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Const.CONTEXT = ShowLocation.this;
		setContentView(R.layout.show_location);
		
		Lat = getIntent().getExtras().getString("Lat");
		Long = getIntent().getExtras().getString("Long");
		
		
		this.pageTitle = (PageTitle) findViewById(R.id.pageTitle);
		this.pageTitle.init();
		this.pageTitle.txtPageTitle.setText("Location on Map");
		
		
		
		GeoPoint point = new GeoPoint((int) (Double.parseDouble(Lat) * 1E6),(int) (Double.parseDouble(Long) * 1E6));
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		MapOverlay mapOverlay = new MapOverlay();
		mapOverlay.setPointToDraw(point);
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);
		mapController.animateTo(point);
		mapController.setZoom(16);
		mapView.invalidate();
	      
		 
		  
		  
	}
	 
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	class MapOverlay extends Overlay
	{
	  private GeoPoint pointToDraw;

	  public void setPointToDraw(GeoPoint point) {
	    pointToDraw = point;
	  }

	  public GeoPoint getPointToDraw() {
	    return pointToDraw;
	  }
	  
	  @Override
	  public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
	    super.draw(canvas, mapView, shadow);           

	    // convert point to pixels
	    Point screenPts = new Point();
	    mapView.getProjection().toPixels(pointToDraw, screenPts);

	    // add marker
	    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
	    canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 24, null);    
	    return true;
	  }
	}

	 }

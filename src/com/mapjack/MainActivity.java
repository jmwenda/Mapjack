package com.mapjack;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.common.PreferencesFacade;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

public class MainActivity extends ActionBarActivity {
	// name of the map file in the external storage
	        private static final String MAPFILE = "/locast/germany.map";
	        
	        private MapView mapView;        
	        private TileCache tileCache;
	        private TileRendererLayer tileRendererLayer;


	        @Override
	        protected void onCreate(Bundle savedInstanceState) {
	                super.onCreate(savedInstanceState);
	                
	                AndroidGraphicFactory.createInstance(this.getApplication());

	                this.mapView = new MapView(this);
	                setContentView(this.mapView);

	                this.mapView.setClickable(true);
	                this.mapView.getMapScaleBar().setVisible(true);
	                this.mapView.setBuiltInZoomControls(true);
	                this.mapView.getMapZoomControls().setZoomLevelMin((byte) 10);
	                this.mapView.getMapZoomControls().setZoomLevelMax((byte) 20);

	                // create a tile cache of suitable size
	                this.tileCache = AndroidUtil.createTileCache(this, "mapcache",
	                                mapView.getModel().displayModel.getTileSize(), 1f, 
	                                this.mapView.getModel().frameBufferModel.getOverdrawFactor());
	        }

	        @Override
	        protected void onStart() {
	                super.onStart();
	                
	                this.mapView.getModel().mapViewPosition.setCenter(new LatLong(52.517037, 13.38886));
	                this.mapView.getModel().mapViewPosition.setZoomLevel((byte) 12);

	                // tile renderer layer using internal render theme
	                this.tileRendererLayer = new TileRendererLayer(tileCache,
	                                this.mapView.getModel().mapViewPosition, false, AndroidGraphicFactory.INSTANCE);
	                tileRendererLayer.setMapFile(getMapFile());
	                tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
	                
	                // only once a layer is associated with a mapView the rendering starts
	                this.mapView.getLayerManager().getLayers().add(tileRendererLayer);

	        }

	        @Override
	        protected void onStop() {
	                super.onStop();
	                this.mapView.getLayerManager().getLayers().remove(this.tileRendererLayer);
	                this.tileRendererLayer.onDestroy();
	        }

	        @Override
	        protected void onDestroy() {
	                super.onDestroy();
	                this.tileCache.destroy();
	        }
	        
	        private File getMapFile() {
	                File file = new File(Environment.getExternalStorageDirectory(), MAPFILE);
	                return file;
	       }
}

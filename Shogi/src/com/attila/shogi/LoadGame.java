package com.attila.shogi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LoadGame extends ListActivity {
	
	byte [] buffer = new byte[ 1000 ];

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		String[] files = fileList();
		
		ListView list = getListView();
		
		final Context k = this;
		
		list.setOnItemLongClickListener( new OnItemLongClickListener( ) {
			public boolean onItemLongClick( AdapterView<?> parent, View view, int position, long id ) {
				
				final int p = position;
				
				AlertDialog.Builder builder2 = new AlertDialog.Builder( k );
				builder2.setMessage( "Delete?" )
				       .setPositiveButton( "No", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   dialog.cancel();
				           }
				       })
				       .setNegativeButton( "Yes", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   deleteGame( p );
				        	   dialog.cancel( );
				           }
				       });
				AlertDialog alert2 = builder2.create();
				alert2.show( );
		
				return true;
			}
		});
		
		this.setListAdapter( new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, files ) );
	}

	private void deleteGame( int position ){
		Object file = this.getListAdapter().getItem(position);
		
		deleteFile( file.toString() );
		
		String[] files = fileList();
		
		this.setListAdapter( new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, files ) );
	}
	
	/* Load drop board */
	private void loadGame( String buffer, String file )
	{
	   Intent iGame = new Intent( this, ShogiGame.class );
	   iGame.putExtra( "CurBoard", buffer.trim().split( "END_OF_GAMEBOARD")[ 0 ] );
 	   iGame.putExtra( "MoveList", buffer.split( "END_OF_GAMEBOARD")[ 1 ].split( "END_OF_MOVELIST")[ 0 ] );
 	   iGame.putExtra( "DropBox", buffer.split( "END_OF_GAMEBOARD")[ 1 ].split( "END_OF_MOVELIST")[ 1 ] );
 	   iGame.putExtra( "SaveName", file );
 	   startActivity( iGame );
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick( l, v, position, id );

		final Object file = this.getListAdapter().getItem(position);
		
		try {
			FileInputStream fos = openFileInput( file.toString() );
			fos.read( buffer );
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AlertDialog.Builder builder2 = new AlertDialog.Builder( this );
		builder2.setMessage( new String( buffer ).trim() + "\n" )
		       .setPositiveButton( "Cancel", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		           }
		       })
		       .setNegativeButton( "Load", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   loadGame( new String( buffer ), file.toString() );
		        	   dialog.cancel( );
		           }
		       });
		AlertDialog alert2 = builder2.create();
		alert2.show( );
	}
	
}

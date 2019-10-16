/*
 * Created by Gordon on 13/05/2017.
 */
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class Main extends Application
{

    private static final Color color = Color.web( "#FF0000" );
    private Button button3 = new Button( "选择图片文件夹" );
    private Button startBtn = new Button( "开始" );
    private DropShadow shadow = new DropShadow();
    private Label label = new Label();
    private Label dirLabel = new Label();
    private Label descLabel = new Label();
    private String dirPath;
    private String moveRule;
    private File chosenDir;
//    private final Desktop desktop = Desktop.getDesktop();

    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    public static void main( String[] args )
    {
        launch( args );
    }

    @Override
    public void start( Stage stage )
    {
        Scene scene = new Scene( new Group() );
        stage.setTitle( "V房团" );
        stage.setWidth( 800 );
        stage.setHeight( 300 );

        label.setFont( Font.font( "Times New Roman", 22 ) );
        label.setTextFill( color );

        dirLabel.setFont( Font.font( "Times New Roman", 22 ) );
        dirLabel.setTextFill( color );

        descLabel.setFont( Font.font( "Times New Roman", 22 ) );
        descLabel.setTextFill( color );


        VBox vbox = new VBox();
        vbox.setLayoutX( 20 );
        vbox.setLayoutY( 20 );
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();


        Button button2 = new Button( "规则文本" );
        button2.setOnAction( ( ActionEvent e ) ->
        {
            fileChooser.setTitle( "选择文件规则文本" );
            File file = fileChooser.showOpenDialog( stage );
            if( null != file )
            {
                try
                {
                    moveRule = FileUtils.readFileToString( file, "UTF-8" );
                    String filePath = file.getAbsolutePath();
                    label.setText( filePath );
                }
                catch( IOException e1 )
                {
                    e1.printStackTrace();
                    label.setText( "选取规则文本出错！" );
                }
            }
        } );

        addEventListener();

        button2.addEventHandler( MouseEvent.MOUSE_ENTERED,  e ->
            button2.setEffect( shadow )
        );

        button2.addEventHandler( MouseEvent.MOUSE_EXITED, ( MouseEvent e ) ->
            button2.setEffect( null )
        );
        button3.addEventHandler( MouseEvent.MOUSE_ENTERED, ( MouseEvent e ) ->
            button3.setEffect( shadow )
        );

        button3.addEventHandler( MouseEvent.MOUSE_EXITED, ( MouseEvent e ) ->
            button3.setEffect( null )
        );

        button3.setOnAction( ( ActionEvent e ) ->
        {
            chosenDir = directoryChooser.showDialog( stage );
            if( chosenDir != null )
            {
                dirPath = chosenDir.getAbsolutePath();
                dirLabel.setText( dirPath );
                System.out.println( dirPath );
            }
            else
            {
                String result = "没有文件夹被选中";
                dirLabel.setText( result );
                System.out.println( result );
            }
        } );

        hBox1.getChildren().add( button2 );
        label.setLayoutY( 30 );
        hBox1.getChildren().add( label );
        hBox1.setSpacing( 10 );
        hBox1.setAlignment( Pos.BOTTOM_LEFT );

        hBox2.getChildren().add( button3 );
        dirLabel.setLayoutY( 30 );
        descLabel.setLayoutY( 60 );
        hBox2.getChildren().add( dirLabel );
        hBox2.getChildren().add( descLabel );
        hBox2.setSpacing( 25 );

        vbox.getChildren().add( hBox1 );
        hBox2.setLayoutY( 50 );
        vbox.getChildren().add( hBox2 );
        vbox.getChildren().add( startBtn );
        vbox.setSpacing( 10 );
        ( (Group) scene.getRoot() ).getChildren().add( vbox );

        stage.setScene( scene );
        stage.show();
    }

    private void addEventListener()
    {
        startBtn.setOnAction( ( ActionEvent e ) ->
        {
            if( null == moveRule )
            {
                label.setText( "请先选取规则文本！" );
                return;
            }
            if( null == dirPath )
            {
                dirLabel.setText( "请先选择文件夹！" );
                return;
            }
            String[] paths = moveRule.split( "\n" );

            Main.this.move( paths );
        } );
    }

    private void move( String[] paths )
    {
        String[] extensions = { "json", "png", "jpg", "fnt", "mp3" };
        Iterator<File> fileIterator = FileUtils.iterateFiles( chosenDir, extensions, true );
//        int length = paths.length;
        while( fileIterator.hasNext() )
        {
            File file = fileIterator.next();
            if( !file.isFile() )
            {
                continue;
            }
            for( String path : paths )
            {
                String name = file.getName();
                if( !path.contains( name ) )
                {
                    continue;
                }

                System.out.println( file.getName() + " 移动到 " + path );
                try
                {
                    FileUtils.moveFileToDirectory( file, new File( path.split( name )[0] ), true );
                }
                catch( IOException exception )
                {
                    System.out.println( file.getName() + " 移动到 " + path + "出错" );
                    exception.printStackTrace();
                }
                break;
            }
            /*for( int i = 0; i < length; i++ )
            {
                String path = paths[i];
                String name = file.getName();
                if( !path.contains( name ) )
                {
                    continue;
                }

                System.out.println( file.getName() + " 移动到 " + path );
                try
                {
                    FileUtils.moveFileToDirectory( file, new File( path.split( name )[0] ), true );
                }
                catch( IOException exception )
                {
                    System.out.println( file.getName() + " 移动到 " + path + "出错" );
                    System.out.println( exception );
                }
                break;
            }*/
        }
    }

    /*private void openFile( File file )
    {
        EventQueue.invokeLater( () ->
        {
            try
            {
                desktop.open( file );
            }
            catch( IOException ex )
            {
                Logger.getLogger( Main.
                        class.getName() ).
                        log( Level.SEVERE, null, ex );
            }
        } );
    }*/
}
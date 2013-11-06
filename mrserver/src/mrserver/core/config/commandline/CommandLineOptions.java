package mrserver.core.config.commandline;

import java.util.Arrays;
import java.util.Collection;
import java.util.ListIterator;

import mrserver.core.Core;
import mrserver.core.config.commandline.options.BotControl;
import mrserver.core.config.commandline.options.BotPorts;
import mrserver.core.config.commandline.options.GraphicsPort;
import mrserver.core.config.commandline.options.Help;
import mrserver.core.config.commandline.options.ScenarioClass;
import mrserver.core.config.commandline.options.ScenarioConfigCmdLine;
import mrserver.core.config.commandline.options.ScenarioConfigFile;
import mrserver.core.config.commandline.options.ScenarioLibrary;
import mrserver.core.config.commandline.options.ServerConfigFile;
import mrserver.core.config.commandline.options.ServerName;
import mrserver.core.config.commandline.options.Vision;
import mrserver.core.config.commandline.options.parse.ParseOption;
import mrserver.core.config.file.ConfigFileReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Klasse zum Parsen und Weiterverarbeiten der Kommandozeile. Nutzt Apache-Cli. 
 * 
 * @author Eike Petersen
 * @since 0.1
 * @version 0.1
 *
 */
@SuppressWarnings("serial")
public class CommandLineOptions extends Options{
    
	private static CommandLineOptions INSTANCE;

    public static CommandLineOptions getInstance() {
        
        if( CommandLineOptions.INSTANCE == null){
            Core.getLogger().trace( "Creating CommandLineOptions-instance." );
            CommandLineOptions.INSTANCE = new CommandLineOptions();
        }

        Core.getLogger().trace( "Retrieving CommandLineOptions-instance." );
        return CommandLineOptions.INSTANCE;
        
    }
	
    private CommandLine mCommandLine;

	private CommandLineOptions(){
        
        super();
        
        OptionGroup vCommandLine = new OptionGroup(), vConfigFile = new OptionGroup();
        
        vCommandLine.addOption( new ServerName() );
        
        addOption( new BotControl() );
        addOption( new Vision() );
        addOption( new BotPorts() );
        addOption( new GraphicsPort() );
        
        addOption( new ScenarioClass() );
        addOption( new ScenarioLibrary() );
        addOption( new ScenarioConfigFile() );
        addOption( new ScenarioConfigCmdLine() );

        addOption( new ServerConfigFile() );
        addOption( new Help() ); // sollte an letzter stelle eingefügt werden, da sie dann wahrscheinlich als erstes ausgeführt wird
        
        
    }
	
	public void close(){
		
		INSTANCE = null;
		
	}
	
	/**
     * Verarbeitet die einzelnen Kommandozeilenargumente. Dabei wird auch überprüft ob alle
     * essentiellen Argumente vorhanden sind. Die einzelnen Argumente wurden vorher in ihren
     * respektiven Optionen definiert.
     * 
     * @since 0.4
     * @param aArgumente die Kommandozeile als String-Array
     * 
     * @return true ob der Server direkt starten oder auf einen Startbefehl warten soll
     */
    
	public boolean parseCommandLineArguments( String[] aArguments ) {

        Core.getLogger().debug( "Parsing commandline: " + Arrays.toString( aArguments ) );

        try {

            parseOptions( aArguments );
            
            // alle übergebenen Optionen implementieren das Interface ParseOption
            for( @SuppressWarnings("unchecked") ParseOption aOption : (Collection<ParseOption>) getOptions() ){
            	
            	aOption.parse( mCommandLine );
            	
            }

        } catch ( MissingOptionException vMissingOptionExceptions ) {

            Options vMissingCommandLineOptions = new Options();

            for ( Object s : vMissingOptionExceptions.getMissingOptions().toArray() ) {

                vMissingCommandLineOptions.addOption( getOption( (String) s ) );

            }

            showCommandlineHelp( "Folgende benötigte Argumente wurden nicht übergeben:", vMissingCommandLineOptions );

        } catch ( ParseException vParseExceptions ) {
            
            Core.getLogger().error( "Fehler beim Parsen der Kommandozeile", vParseExceptions );

        } catch ( Exception vAllExceptions ) {

            Core.getLogger().error( "Unerwarteter Fehler", vAllExceptions );

        }
        
        Core.getInstance().close();
        return false;

    }
    
    private void parseOptions( String[] aArguments ) throws ParseException {
                
        CommandLineParser vParser = new ExtendedGnuParser( true );
        mCommandLine = vParser.parse( this, aArguments, false);
        
    }
    
    public void showCommandlineHelp( String aHelpString, Options aCommandLineOptions ) {

        HelpFormatter vHelpFormatter = new HelpFormatter();
        vHelpFormatter.setWidth( 120 );
        vHelpFormatter.setLongOptPrefix( "-" );
        vHelpFormatter.setSyntaxPrefix( "" );
        vHelpFormatter.printHelp( aHelpString, aCommandLineOptions );

        System.exit( 0 );
    }

    class ExtendedGnuParser extends GnuParser {

        private boolean mIgnoreUnrecognizedOption;

        public ExtendedGnuParser(final boolean ignoreUnrecognizedOption) {
            mIgnoreUnrecognizedOption = ignoreUnrecognizedOption;
        }

        @SuppressWarnings("rawtypes")
		@Override
        protected void processOption(final String aArgument, final ListIterator aIter) throws ParseException {
            boolean hasOption = getOptions().hasOption( aArgument );

            if ( hasOption || !mIgnoreUnrecognizedOption ) {
                super.processOption( aArgument, aIter );
            }
        }

    }
    
}

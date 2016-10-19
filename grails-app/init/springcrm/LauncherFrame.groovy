/*
 * LauncherFrame.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package springcrm

import groovy.transform.CompileStatic
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.font.TextLayout
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.text.MessageFormat
import javax.imageio.ImageIO
import javax.swing.*
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ApplicationContextEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.ContextStartedEvent


/**
 * The class {@code LauncherFrame} displays a GUI window which shows the
 * loading progress of the application and allows display of the web site in a
 * browser.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class LauncherFrame implements ApplicationListener<ApplicationContextEvent> {

    //-- Constants ------------------------------

    /**
     * The URL which should be called in browser to see the web site of the
     * application.
     */
    public static final String BROWSER_URL = 'http://localhost:8080'


    //-- Class fields ---------------------------

    private static final Log log = LogFactory.getLog(this)


    //-- Fields ---------------------------------

    /**
     * The button to show the web site in browser.
     */
    JButton launchBtn

    /**
     * The resource bundle containing the localized messages.
     */
    ResourceBundle resourceBundle


    //-- Constructors ---------------------------

    /**
     * Creates and displays a new launcher window.
     */
    LauncherFrame() {
        resourceBundle = ResourceBundle.getBundle('launcher')

        // set some OSX properties
        System.setProperty 'apple.laf.useScreenMenuBar', 'true'
        System.setProperty 'apple.awt.textantialiasing', 'true'

        initialize()
    }


    //-- Public methods -------------------------

    /**
     * Called when an event related to the application context occurred.  The
     * method enables the launch button when the application context has been
     * initialized.
     *
     * @param event the occurred event
     */
    @Override
    void onApplicationEvent(ApplicationContextEvent event) {
        log.debug "Received application event ${event.getClass().name}"
        if (event instanceof ContextStartedEvent ||
            event instanceof ContextRefreshedEvent)
        {
            launchBtn.enabled = true
            launchBtn.icon = null
            launchBtn.text = getString('btn.launch')
        }
    }


    //-- Non-public methods ---------------------

    /**
     * Changes the image with the given URL by writing the stated version
     * number on it.
     *
     * @param url       the given URL of the image
     * @param version   the given version number
     * @return          the modified image
     */
    private static Image applyVersionToImage(URL url, String version) {
        BufferedImage image = ImageIO.read(url)
        Graphics g = image.graphics
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g
            g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB
            )
            Font font = new Font(Font.SANS_SERIF, 0, 12)
            g.font = font
            g.color = new Color(0x333333)
            Rectangle2D r =
                new TextLayout(version, font, g2.fontRenderContext).bounds
            int x = image.width - (int) r.width
            int y = image.height - (int) r.height + 2
            g.drawString version, x, y
        }
        g.dispose()

        image
    }

    /**
     * Gets a localized string from the resource bundle and substitutes any
     * placeholders with the given arguments.
     *
     * @param key       the key of the message in the resource bundle
     * @param arguments any arguments to substitute placeholders; may be
     *                  {@code null}
     * @return          the localized string
     */
    private String getString(String key, Object... arguments) {
        String s = resourceBundle.getString(key)
        if (arguments != null) {
            s = MessageFormat.format(s, arguments)
        }

        s
    }

    /**
     * Initializes the window with all its controls.
     */
    private void initialize() {
        UIManager.setLookAndFeel UIManager.systemLookAndFeelClassName

        String version = getString('app.version')
        JFrame frame = new JFrame("${getString('app.title')} ${version}")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane = new JPanel()
        frame.layout = new BorderLayout()

        Icon icon = new ImageIcon(
            applyVersionToImage(System.getResource('/logo.png'), version)
        )
        JLabel logo = new JLabel(icon)
        logo.border = BorderFactory.createEmptyBorder(30, 50, 30, 50)
        frame.add logo, BorderLayout.PAGE_START

        icon = new ImageIcon(System.getResource('/spinner.gif'))
        launchBtn = new JButton(getString('btn.launching'))
        launchBtn.enabled = false
        launchBtn.icon = icon
        launchBtn.disabledIcon = icon
        launchBtn.iconTextGap = 20
        launchBtn.font = launchBtn.font.deriveFont(18.0f)
        launchBtn.minimumSize = new Dimension(100, 50)
        launchBtn.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.desktop
                if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                    JOptionPane.showMessageDialog(
                        frame, getString('message.url', BROWSER_URL)
                    )
                    return
                }

                desktop.browse new URI(BROWSER_URL)
            }
        })
        frame.add launchBtn, BorderLayout.PAGE_END

        frame.pack()
        frame.visible = true
    }
}

/**
 *
 */
package org.dajoo.kernel;

import org.picocontainer.Startable;

/**
 * @author Mountain
 *
 */
public interface Daemon extends Startable {

    public void run() throws DaemonException;

}

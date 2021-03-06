/**
 * (X)ToolbarManager.java
 *
 * Copyright (C) 2006-2009 Mingli Yuan
 * http://www.dajoo.org/
 * http://dajoo.sourceforge.net/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * http://www.gnu.org/copyleft/gpl.html
 *
 */

package org.dajoo.frame;

import java.util.ArrayList;

public class ToolbarManager {

    private ToolbarButton[] buttons;

    public ToolbarManager() {
        //NOOP
    }

    public ToolbarManager(ToolbarButton[] b) {
        buttons = b;
    }

    public ToolbarButton[] getButtonsForPath(String path) {
        int len = buttons != null ? buttons.length : 0;
        ArrayList<ToolbarButton> list = new ArrayList<ToolbarButton>(len);
        for (int i = 0; i < len; i++) {
            if(buttons[i].isApplicable(path))
                list.add(buttons[i]);
        }
        ToolbarButton[] returnedBtns = new ToolbarButton[list.size()];
        returnedBtns = list.toArray(returnedBtns);
        return returnedBtns;
    }

}

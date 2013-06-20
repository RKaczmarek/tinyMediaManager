/*
 * Copyright 2012 - 2013 Manuel Laggner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinymediamanager.ui;

import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import org.tinymediamanager.core.IMessageListener;
import org.tinymediamanager.core.MediaEntity;
import org.tinymediamanager.core.MediaFile;
import org.tinymediamanager.core.Message;
import org.tinymediamanager.core.Message.MessageLevel;

/**
 * Class UIMessageListener used to push the messaged to the EDT
 * 
 * @author Manuel Laggner
 */
public class UIMessageListener implements IMessageListener {
  private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages", new UTF8Control()); //$NON-NLS-1$

  /*
   * (non-Javadoc)
   * 
   * @see org.tinymediamanager.core.IMessageListener#pushMessage(org.tinymediamanager.core.Message)
   */
  @Override
  public void pushMessage(final Message message) {
    // only display errors in UI
    if (message.getMessageLevel() != MessageLevel.ERROR) {
      return;
    }

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        String msg = "";
        String title = "";

        // get title
        if (message.getMessageSender() instanceof MediaEntity) {
          // mediaEntity title: eg. Movie title
          MediaEntity me = (MediaEntity) message.getMessageSender();
          title = me.getTitle();
        }
        else if (message.getMessageSender() instanceof MediaFile) {
          // mediaFile: filename
          MediaFile mf = (MediaFile) message.getMessageSender();
          title = mf.getFilename();
        }
        else {
          title = message.getMessageSender().toString();
        }

        // get message
        try {
          // try to get a localized version
          msg = BUNDLE.getString(message.getMessageId());
        }
        catch (Exception e) {
          // simply take the id
          msg = message.getMessageId();
        }
        MainWindow.getActiveInstance().addMessage(title, msg);
      }
    });
  }
}
/*
 * init.js
 * Initialize scripting.
 *
 * $Id$
 *
 * Copyright (c) 2011, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
 */


/**
 * @fileOverview    Contains basic initializations of scripting which is used
 *                  within this application.
 * @author          Daniel Ellermann
 * @version         0.9
 */


/**
 * Contains all JavaScript code for the SpringCRM application.
 *
 * @namespace   Contains all JavaScript code for the SpringCRM application.
 */
var SPRINGCRM = SPRINGCRM || (function ($) {

    "use strict";

    var addMessages,
        getMessage,
        messages = {};

    /**
     * Adds the given messages to the internal message store.
     *
     * @name SPRINGCRM.addMessages
     * @param {Object} msgs the messages to add; the messages must be
     *                      key/value pairs
     * @function
     * @static
     */
    addMessages = function (msgs) {
        $.extend(messages, msgs);
    };

    /**
     * Gets the message with the given key from the internal message store.
     *
     * @name SPRINGCRM.getMessage
     * @param {String} key  the given message key
     * @returns {String}    the internationalized message;
     *                      <code>undefined</code> if no message with the
     *                      given key was found
     * @function
     * @static
     */
    getMessage = function (key) {
        return messages[key];
    };


    return {
        addMessages: addMessages,
        getMessage: getMessage
    };
}(jQuery));

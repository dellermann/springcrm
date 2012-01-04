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


var $L = function (key) {

        "use strict";

        return $L._messages[key.replace(/\./g, "_")];
    },
    SPRINGCRM = SPRINGCRM || {};

$L._messages = {};
$L.addMessages = function (msgs) {

    "use strict";

    jQuery.extend($L._messages, msgs);
};

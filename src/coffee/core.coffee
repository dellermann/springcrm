#
# core.coffee
#
# Copyright (c) 2011-2013, Daniel Ellermann
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#


$ = jQuery
$html = $("html")
decimalSeparator = $html.data("decimal-separator") or ","
groupingSeparator = $html.data("grouping-separator") or "."
numFractions = $html.data("num-fraction-digits") or 2


# Formats this date with either the given user-defined format or the localized
# date and time format as specified in the messages `dateFormat` and
# `timeFormat`.  If using a user-defined format, the following placeholders may
# be used:
#
# * `d`. the day of month (1..31)
# * `dd`. the two-digit day of month (1..31), optionally filled with zeros
# * `M`. the month number (1..12)
# * `MM`. the two-digit month number (1..12), optionally filled with zeros
# * `y`. the year without century
# * `yy`. the two-digit year without century
# * `yyyy`. the four-digit year with century
# * `H`. the hour (0..23)
# * `HH`. the two-digit hour (0..23), optionally filled with zeros
# * `m`. the minute (0..59)
# * `mm`. the two-digit minute (0..59), optionally filled with zeros
#
# @param {String} format  the format specification used to format the date.  This may be either a user-defined format using the placeholders listed above or any of the given types `date`, `time`, and `datetime`, which obtain the actual format from the localized strings.
# @return {String}        the formatted date
#
Date::format = (format = "datetime") ->
  if (format is "date") or (format is "time") or (format is "datetime")
    fmt = ""
    fmt += $L("default.format.date") if (format is "date") or (format is "datetime")
    fmt += " " if format is "datetime"
    fmt += $L("default.format.time") if (format is "time") or (format is "datetime")
    format = fmt

  pad = (x) ->
    s = x.toFixed()
    s = "0#{s}" if s.length < 2
    s

  d = @
  regexp = /[^dMyHm]/
  res = ""
  while format
    if regexp.test(format)
      token = RegExp.leftContext
      delimiter = RegExp.lastMatch
      format = RegExp.rightContext
    else
      token = format
      delimiter = ""
      format = ""

    switch token
      when "d"
        res += d.getDate()
      when "dd"
        res += pad(d.getDate())
      when "M"
        res += d.getMonth() + 1
      when "MM"
        res += pad(d.getMonth() + 1)
      when "y"
        res += d.getYear()
      when "yy"
        res += pad(d.getYear() % 100)
      when "yyy", "yyyy"
        res += String(d.getFullYear())
      when "H"
        res += d.getHours()
      when "HH"
        res += pad(d.getHours())
      when "m"
        res += d.getMinutes()
      when "mm"
        res += pad(d.getMinutes())
    res += delimiter
  res

# Formats this number with the given number of fraction digits and the decimal
# and grouping separator as specified in the attributes `decimal-separator` and
# `grouping-separator`, respectively, in the `<html>` tag.  If the separators
# are not set, a dot (.) is used as decimal separator and a comma (,) as
# grouping separator.
#
# @param {Number} n the precision; if not set or `null` the precision remains unchanged
# @return {String}  the formatted number
# @since            1.3
#
Number::format = (n = null) ->
  gs = groupingSeparator

  num = this
  numSgn = (if (n is null) then num else num.round(n))
  sgn = (if numSgn < 0 then "-" else "")
  n = (if (n is null) then undefined else Math.abs(n))
  num = Math.abs(+num or 0)
  int = String(parseInt(num = (if isNaN(n) then num.toString() else num.toFixed(n)), 10))
  pos = (if (pos = int.length) > 3 then pos % 3 else 0)

  frac = ""
  if n isnt 0
    dotPos = num.indexOf(".")
    if dotPos >= 0
      frac = Math.abs(num.substring(dotPos))
      frac = (if isNaN(n) then frac.toString() else frac.toFixed(n))
      frac = decimalSeparator + frac.substring(2)
  sgn + ((if pos then int.substr(0, pos) + gs else "")) +
    int.substr(pos).replace(/(\d{3})(?=\d)/g, "$1" + gs) + frac

# Formats the given number as currency value, that is with the number of
# fraction digits specified in attribute `num-fraction-digits` of the `<html>`
# tag.  If the attribute is not specified a number of 2 fraction digits are
# used.  Furthermore, all formatting used in method `format` is used as well.
#
# @return {String}  the formatted currency value
# @see              Number#format
# @since            1.3
#
Number::formatCurrencyValue = ->
  if isNaN(this) or not isFinite(this) then '---' else @format numFractions

# Rounds this number to the given number of fraction digits.
#
# @param {Number} n the given number of fraction digits
# @return {Number}  the rounded number
#
Number::round = (n = numFractions) ->
  power = Math.pow 10, n
  Math.round(this * power) / power

# Escapes all characters in the given string which have special meaning in
# regular expressions.
#
# @param {String} s the given string
# @return {String}  the string with escaped special characters suitable for a regular expression
# @since            1.3
#
RegExp.escape = (s) ->
  s.replace /[\-\/\\\^$*+?.()|\[\]{}]/g, "\\$&"

# Compares this string to the given one.
#
# @param {String} s the string to compare
# @return {Number}  -1 if this string is less than the given one; 1 if this string is greater than the given one; 0 if both the strings are equal
# @since            1.4
#
String::compare = (s) ->
  (if s < this then -1 else (if s > this then 1 else 0))

# Parses this string as a date and/or time value in either the given
# user-defined format or the localized date and time format as specified
# in the messages `dateFormat` and `timeFormat`.  If using a user-defined
# format, the following placeholders may be used:
#
# * `d` or `dd`. the day of month (1..31)
# * `M` or `MM`. the month number (1..12)
# * `y`, `yy`, `yyy`, or `yyyy`. the year without century
# * `H` or `HH`. the hour (0..23)
# * `m` or `mm`. the minute (0..59)
#
# @param {String} format    the format specification used to parse the date.  This may be either a user-defined format using the placeholders listed above or any of the given types `date`, `time`, and `datetime`, which obtain the actual format from the localized strings.
# @param {Number} baseYear  the year which acts as limit for year specifications without century: years before the base year are treated as after 2000, all other years before 2000
# @return {Date}            the parsed date; `null` if this string is empty
# @throw Error              if this string does not represent a valid date according to the specified or default format
#
String::parseDate = (format = "datetime", baseYear = 35) ->
  s = this
  return null unless $.trim(s)

  if (format is "date") or (format is "time") or (format is "datetime")
    fmt = ""
    fmt += $L("default.format.date") if (format is "date") or (format is "datetime")
    fmt += " " if format is "datetime"
    fmt += $L("default.format.time") if (format is "time") or (format is "datetime")
    format = fmt

  day = 1
  month = 0
  year = 1970
  hours = 0
  minutes = 0

  pos = 0
  regexp = /[^dMyHm]/
  while format
    if regexp.test format
      token = RegExp.leftContext
      pos = s.indexOf RegExp.lastMatch
      if (pos < 0) or (pos + 1 >= s.length)
        throw new Error("Invalid date or time format: #{s}")
      part = s.substring 0, pos
      s = s.substring pos + 1
      format = RegExp.rightContext
    else
      token = format
      part = s
      format = ""

    switch token
      when "d", "dd"
        day = parseInt(part, 10)
      when "M", "MM"
        month = parseInt(part, 10) - 1
      when "y", "yy", "yyy", "yyyy"
        year = parseInt(part, 10)
        year += (if (year < baseYear) then 2000 else 1900) if year < 100
      when "H", "HH"
        hours = parseInt(part, 10)
      when "m", "mm"
        minutes = parseInt(part, 10)

  if isNaN(year) or isNaN(month) or isNaN(day) or isNaN(hours) or isNaN(minutes)
    throw new Error("Invalid date or time format: #{s}")

  new Date(year, month, day, hours, minutes, 0, 0)

# Parses this string as number using the decimal and grouping separator
# specified in the attributes `decimal-separator` and `grouping-separator`,
# respectively, in the`<html>` tag.  If the separators are not set, a dot (.)
# is used as decimal separator and a comma (,) as grouping separator.
#
# @return {Number}  the parsed number
# @since            1.3
#
String::parseNumber = ->
  reD = new RegExp(RegExp.escape(decimalSeparator), "g")
  reG = new RegExp(RegExp.escape(groupingSeparator), "g")
  s = this.toString()
  (if (s is "") then 0 else parseFloat(s.replace(reG, "").replace(reD, ".")))

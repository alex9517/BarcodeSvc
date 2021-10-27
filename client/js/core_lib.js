//  Created : 2019-Nov-25;
// Modified : 2020-Feb-20;

var month3char = [
  'Jan','Feb','Mar','Apr','May','Jun',
  'Jul','Aug','Sep','Oct','Nov','Dec',];

var getCurrDate = function() {
  var date = new Date();
  var day = date.getDate();

  if (day < 10) {
    day = String('0' + day);
  }
  return(date.getFullYear() + '-' + month3char[date.getMonth()] + '-' + day);
};


var getCurrTime = function() {
  var date = new Date();
  var hours = date.getHours();
  var minutes = date.getMinutes();

  if (hours < 10) {
    hours = String('0' + hours);
  }
  if (minutes < 10) {
    minutes = String('0' + minutes);
  }
  return(hours + ':' + minutes);
};


var getCharMonthDateString = function(s) {
  // Note! 's' must be ISO date strings "YYYY-MM-DD"

  // Note that new Date(null) is equiv to new Date(0)
  // which is "Thu Jan 01 1970 04:00:00 GMT+0400 (+04)"

  if (!s || s.trim().length === 0) return '?';

  try {
    var d = new Date(s);
    console.log('Converted to : ' + d);
    var day = d.getDate();

    if (day < 10) {
      day = String('0' + day);
    }
    var ds = d.getFullYear() + '-' + month3char[d.getMonth()] + '-' + day;
      // console.log("New date string : " + ds);
    return ds;
  }
  catch(err) {
    console.log('Exception in getCharMonthDateString() : ' + err);
    return '?';
  }
};


var getAge = function(s1, s2) {
  // Note! 's1' - birth date, 's2' - pass date
  //   (must be more than 's1' when compared as dates),
  //   and both 's*' must be ISO date strings "YYYY-MM-DD".

  // Note! Date(null) is equiv to Date(0) which
  //   is "Thu Jan 01 1970 04:00:00 GMT+0400 (+04)".

  if (!s1 || s1.trim().length === 0) {
    console.log('Birth date is bad! Age cannot be calculated.');
    return -1;
  }
  try {
    var d2 = new Date();
    var d1 = new Date(s1);
      // console.log('Birth date converted to : ' + d1);

    if (!s2 || s2.trim().length === 0) {
      // console.log('Pass date .. far from now');
    }
    else {
      d2 = new Date(s2);
      console.log('Pass date converted to : ' + d2);
    }
    var numYears = d2.getFullYear() - d1.getFullYear();

    if (numYears <= 0) return -1;

    if (d2.getMonth() < d1.getMonth()) {
      numYears--;
    } else if ((d2.getMonth() == d1.getMonth()) &&
        (d2.getDate() < d1.getDate())) {
      numYears--;
    }
    return numYears;
  }
  catch(err) {
    console.log('Exception in getAge() : ' + err)
    return -1;  // Probably data string format is bad;
  }
};


// Note that 't' is in seconds!
// So, one hour would be t = 3600,
// for one day t = 86400,
// for one week t = 604800,
// for one month t ~ 2592000,
// for one year t ~ 31536000, etc.
// There is no 'forever', only very large numbers!

var setCookie = function(name,value,t) {
  var dat = new Date();
  dat.setTime(dat.getTime() + (t * 1000));
  var expire = '; expires=' + dat.toGMTString();
  document.cookie = name + '=' + escape(value) + expire;
};


var setMemCookie = function(name,value) {
  document.cookie = name + '=' + escape(value);
};


var getCookie = function(cname) {
  var name = cname + '=';
  var ca = document.cookie.split(';');

  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0)==' ') c = c.substring(1);

    if (c.indexOf(name) == 0) {
      return decodeURIComponent(c.substring(name.length,c.length));
    }
  }
  return '';
};


var removeCookie = function(name) {
  setCookie(name, '', -1);
};


var removeCookies = function() {
  setCookie(cookieStyle, '', -1);
  setCookie(cookiePage, '', -1);
  console.log('\'' + cookieStyle + '\' and \'' +
      cookiePage + '\' are removed from cookies.');
};


var base64ToArrayBuffer = function(base64) {
  var binary_string =  window.atob(base64);
  var len = binary_string.length;
  var bytes = new Uint8Array( len );

  for (var i = 0; i < len; i++) {
    bytes[i] = binary_string.charCodeAt(i);
  }
  return bytes.buffer;
};


var arrayBufferToBase64 = function(buffer) {
  var binary = '';
  var bytes = new Uint8Array(buffer);
  var len = bytes.byteLength;

  for (var i = 0; i < len; i++) {
    binary += String.fromCharCode(bytes[i]);
  }
  return window.btoa(binary);
};


var checkMessageLen = function(s) {
  if (s) {
    // The following procedure allows to break a very
    // long line into pieces for better HTML rendering;

    s = s.replace(/":"/g, '": "');
    if (s.length <= 120) return s;
    return s.substr(0,117) + '...';
  }
  return '';
};


var getCurrFileName = function() {
  var currPage = window.location.pathname;
  return currPage.substring(currPage.lastIndexOf('/') + 1);
  // ... currPage.split('/').pop();
};


var getScrollbarWidth = function() {
  var div, body, W = window.browserScrollbarWidth;

  if (W === undefined) {
    body = document.body, div = document.createElement('div');
    div.innerHTML =
        '<div style="width: 50px; height: 50px; position: absolute; ' +
        'left: -100px; top: -100px; overflow: auto;">' +
        '<div style="width: 1px; height: 100px;"></div></div>';
    div = div.firstChild;
    body.appendChild(div);
    W = window.browserScrollbarWidth = div.offsetWidth - div.clientWidth;
    body.removeChild(div);
  }
  return W;
};

// -END-

# Python example

This example simulates a python client retrieving a JIRA issue via Oauth.

## Pre-requisites
* JIRA instance running on http://localhost:8090/jira (or change the URL in the script)
* An issue with key BULK-1
* An OAuth consumer with 'oauth-sample-consumer' as it's key and the public key from rsa.pub
* Python
** pycrypto library
** tlslite library
** oauth2 (which also requires httplib2) - https://github.com/simplegeo/python-oauth2

Simplest way to install the python dependencies:
* Get PIP (http://pypi.python.org/pypi/pip) then
* sudo pip install oauth2
* sudo pip install pycrypto
* Install tlslite manually, since pip doesn't work (run 'sudo python setup.py install' in the tlslite download).

## Running the test

Simply run python app.py.

It will prompt you to authorize the oauth request in your browser. Follow this link and click 'Approve'.
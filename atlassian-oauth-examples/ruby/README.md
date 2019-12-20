# Atlassian 3-legged OAuth Example in Ruby/Sinatra

This example uses [Sinatra](http://sinatrarb.com), a lightweight web framework for Ruby.  
I'm assuming that you already have Ruby set up on your machine (if you're on OS X, it is.
Windows... google it). The first thing you need to do is install Bundler, if you haven't 
already.

    gem install bundler

This will install the necessary gems required. Then run the following inside the ./ruby 
directory to start the server.

    rackup

You can now access the app though:

    http://localhost:9292

To see how it all works, check out the comments inside app.rb.

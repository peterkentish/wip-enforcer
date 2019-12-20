#!/usr/bin/perl

use strict vars;
use Data::Dumper;
use JSON;
use OAuth;

# Create new OAuth object
my $oauth = OAuth->new(prot_version => "1.0",
                       auth_callback => "http://localhost/"); # Callback ignored if prot_version isn't "1.0a"
                            
# Atempt to load access tokens from default file store
# don't treat failure as a critical error and instead continue
$oauth->load_access_token_crypt_from_file("", nocroak => 1);

# Check to see if we got a valid access token from file
if (!$oauth->has_access_token())
{
  print "No saved access token\n";
  
  # Send initial request
  $oauth->request_request_token();

  # Get the authorization url and display it
  my $authUrl = $oauth->generate_auth_request_url();
  print "Auth request URL: $authUrl\n";

  # For 1.0a we need to ask for the verifier, for 1.0 just waiting until they verify is enough
  if ($oauth->prot_version() eq "1.0a")
  {
    print "Enter verifier: ";
    my $verifier = <STDIN>;
    chomp($verifier);

    $oauth->request_access_token($verifier);  
  }
  else
  {
    print "<Press return once you have accepted request>";
    <STDIN>;

    $oauth->request_access_token();  
  }
  
  # Store out the access token to file in an non-human readable form
  $oauth->save_access_token_crypt_to_file();
  
  print "Access token saved: " . join(" - ", $oauth->get_access_token()) . "\n";
}
else
{
  print "Access token restored: " . join(" - ", $oauth->get_access_token()) . "\n";
  
  # Try out a simple request to get all repositories for a named project
  my $project = "CST";
  my $response = $oauth->make_request("GET", 
                                      "rest/api/1.0/projects/" . $project . "/repos",
                                       params  => {limit => 500, start => 0},
                                       headers => {Accepts => "application/json"});
                               
  # If a success convert the results from JSON and dump them, otherwise show an error        
  if ($response->is_success)
  {
    my $result = from_json($response->content);
    print Dumper($result);
  }
  else
  {
    print "Request failed: " . $response->status_line . "\n";
  }
}

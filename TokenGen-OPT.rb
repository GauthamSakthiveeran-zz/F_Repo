#!/usr/bin/env ruby

# This is a tool used to generate embed token signature given the provider api_key, secret, pcode,
# list of embed codes, and expiration

require "base64"
require "digest/sha2"
require 'digest/md5'
require "cgi"
require "open-uri"
require "optparse"

#Token generator
#https://osiris.ooyala.com/backdoor/create_super_token

# Set $account_id to "" if no account ID is needed


$embed_code = ""
$api_key = ""
$pcode =  ""
$api_secret =  ""
$expires =  "1496286630" # 06/01/2017 22:58:29
$signature_hashing_method =  "SHA256"
$SyndGroup ="override_all_synd_groups" #"override_all_synd_groups" #override_synd_groups_in_backlot #leave empty if no need for overrides
$account_id = ""
$country = "ALL"

def generate_signature(ec,key,secret)
  #$epoch = '1419400800000'
  #(Time.now + expires.to_i).to_i
  
  $embed_code = $ec
  $api_key = $key
  $pcode = $key.split(".")[0]
  $api_secret=$secret
  
  if $account_id != "" then
	string_to_sign = "#{$api_secret}GET/v2/publishing_rules/#{$embed_code}api_key=#{$api_key}expires=#{$expires}"
else
	string_to_sign = "#{$api_secret}GET/v2/publishing_rules/#{$embed_code}api_key=#{$api_key}expires=#{$expires}"
end
	  
	  
	puts "Using #{$signature_hashing_method} to sign: #{string_to_sign}"

  if $signature_hashing_method == "MD5"
    p "Signing with MD5...."
    return Base64::encode64(Digest::MD5.digest(string_to_sign))[0..42]
  else
    p "Signing with SHA256...."
    return Base64::encode64(Digest::SHA256.digest(string_to_sign))[0..42]
  end
		   
end

def image_request(signature)
  if $account_id != "" then
	  	url = "?api_key=#{$api_key}&expires=#{$expires}&signature=#{signature}"
	  else
	    url = "?api_key=#{$api_key}&expires=#{$expires}&signature=#{signature}"
	  end
	  
   return url
end	
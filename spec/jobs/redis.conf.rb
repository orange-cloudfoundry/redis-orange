require 'rspec'
require 'json'
require 'bosh/template/test'

describe 'redis job' do
  let(:release) { Bosh::Template::Test::ReleaseDir.new(File.join(File.dirname(__FILE__), '../..')) }
  let(:job) { release.job('redis') }

  describe 'redis.conf' do
    let(:template) { job.template('config/redis.conf') }

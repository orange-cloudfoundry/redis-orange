require 'rspec'
require 'json'
require 'bosh/template/test'

describe 'redis job' do
  let(:release) { Bosh::Template::Test::ReleaseDir.new(File.join(File.dirname(__FILE__), '../..')) }
  let(:job) { release.job('redis') }

  describe 'redis.conf.erb' do
    let(:template) { job.template('redis.conf.erb') }
    it 'toto' do
      config = JSON.parse(template.render('redis.bind'))
      toto = config['redis.bind']
      print 'toto=#{toto}'
    end
  end
end

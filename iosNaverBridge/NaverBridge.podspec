Pod::Spec.new do |s|
  s.name             = 'NaverBridge'
  s.version          = '0.1.0'
  s.summary          = 'Local bridge for Naver login'
  s.homepage         = 'local'
  s.license          = { :type => 'MIT', :text => 'MIT' }
  s.author           = { 'local' => 'local' }
  s.source           = { :path => '.' }

  s.ios.deployment_target = '13.0'
  s.swift_version    = '5.0'

  s.source_files     = 'Sources/**/*.{swift}'
  s.dependency       'NidThirdPartyLogin'
end


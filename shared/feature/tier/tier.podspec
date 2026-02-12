Pod::Spec.new do |spec|
    spec.name                     = 'tier'
    spec.version                  = '1.0.0'
    spec.homepage                 = 'https://your.domain'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'tierKit'
    spec.vendored_frameworks      = 'build/cocoapods/framework/tier.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target    = '12.0'
    spec.dependency 'NMapsGeometry', '1.0.2'
    spec.dependency 'NMapsMap', '3.19.0'
                
    if !Dir.exist?('build/cocoapods/framework/tier.framework') || Dir.empty?('build/cocoapods/framework/tier.framework')
        raise "

        Kotlin framework 'tier' doesn't exist yet, so a proper Xcode project can't be generated.
        'pod install' should be executed after running ':generateDummyFramework' Gradle task:

            ./gradlew :shared:feature:tier:generateDummyFramework

        Alternatively, proper pod installation is performed during Gradle sync in the IDE (if Podfile location is set)"
    end
                
    spec.xcconfig = {
        'ENABLE_USER_SCRIPT_SANDBOXING' => 'NO',
    }
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':shared:feature:tier',
        'PRODUCT_MODULE_NAME' => 'tier',
    }
                
    spec.script_phases = [
        {
            :name => 'Build tier',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../../../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
    spec.resources = ['build/compose/cocoapods/compose-resources']
end
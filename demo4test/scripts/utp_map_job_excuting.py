import os
import zipfile
import shutil

def utp_print(s):
    print"utp>" + s

def command(cmd, visble=True):
    if visble:
        utp_print(cmd)
    ret = os.system(cmd)
    return ret

# return as string list
def command2(cmd, visble=True):
    if visble:
        utp_print(cmd)
    r = os.popen(cmd)
    lines = r.readlines()
    r.close()
    return lines

def createExtractToFolder(extract_to):
    extract_to_path = os.path.join(os.getcwd(), extract_to)
    if not os.path.isdir(extract_to_path):
        os.mkdir(extract_to_path) # result/

def move_out(src, src_temp, dst):
    src_folder = src
    if not os.path.isdir(src):
        src_folder = src_temp
    for f in os.listdir(src_folder):
        src_file = os.path.join(src_folder, f)
        dst_file = os.path.join(dst, f)
        if os.path.isfile(src_file):
            shutil.copy(src_file, dst_file) # dst folder must created.
            utp_print("copy %s to %s complete." %(src_file, dst))

def main():
    apk_path = os.environ.get('debug_apk')#"demo4test-debug.apk" #os.environ.get('debug_apk')
    apk_pkg = "com.tencent.mig.demo4test.SOSOMap" #os.environ.get('debug_pkg')
    test_apk_path = os.environ.get('test_apk')#"demo4test-debug-androidTest-unaligned.apk"#os.environ.get('test_apk')
    test_apk_pkg = "com.tencent.mig.demo4test.SOSOMap.test" #os.environ.get('test_pkg')
    test_class = os.environ.get('test_class')#"com.tencent.mig.demo4test.testcases.SdkBVTTestSuite"
    android_device_serial = os.environ.get('ANDROID_SERIAL')#"192.168.206.102:5555"#os.environ.get('ANDROID_SERIAL')

    target_name = "tencentMapSDKTest"
    zip_on_phone = "/sdcard/" + target_name + "/"
    move_to = "output"
    temp = "temp"
    target_in_temp_folder = os.path.join(temp, target_name)

    print "TestCases:\n" + test_class

    print "Current Apk Path:" + apk_path

    if android_device_serial == "":
        print "Android Device Serial is Empty! Something wrong & Test Abort!"
    else:
        command('adb -s %s shell ls /sdcard/' % (android_device_serial))
        command('adb -s %s shell rm -rf %s' % (android_device_serial,zip_on_phone))
        command('adb -s %s uninstall %s' % (android_device_serial,apk_pkg))
        command('adb -s %s uninstall %s' % (android_device_serial,test_apk_pkg))
        
        s_sdk = command2('adb -s %s shell getprop ro.build.version.sdk' % (android_device_serial))
        sdk = int(s_sdk[0])
        if sdk >= 23:
            command('adb -s %s install -g %s' % (android_device_serial,apk_path))
            command('adb -s %s install -g %s' % (android_device_serial,test_apk_path))
            utp_print('sdk>=23, sdk=%s'  % (sdk))
        else:
            command('adb -s %s install %s' % (android_device_serial,apk_path))
            command('adb -s %s install %s' % (android_device_serial,test_apk_path))
            utp_print('sdk<23, sdk=%s'  % (sdk))

        command('adb  -s %s shell am instrument -w -r \
        -e debug false -e class %s \
        %s/android.support.test.runner.AndroidJUnitRunner'
         % (android_device_serial,test_class,test_apk_pkg))

        # must create folder extract_to first, otherwise 'adb: error: cannot create file/directory' on Linux
        createExtractToFolder(temp)
        createExtractToFolder(move_to)
        
        command('adb -s %s pull %s %s' % (android_device_serial,zip_on_phone,temp))
        command('adb -s %s shell ls /sdcard/' % (android_device_serial))

        move_out(target_in_temp_folder, temp, move_to)

if __name__ == '__main__':
    main()
/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useEffect} from 'react';
import type {Node} from 'react';
import {
  SafeAreaView,
  TouchableOpacity,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';

import ZoomUs from 'react-native-zoom-us';

const ZOOM_CONFIG = {
  ZOOM_PUBLIC_KEY: 'shah3TQPdYodazVsn76A6Up6mdwWmhW6Mb9v',
  ZOOM_PRIVATE_KEY: 'up1RTZrNOHatdJHI5KijERP7mDS8tCLNkjXe',
  ZOOM_DOMAIN: 'zoom.us',
};

const App: () => Node = () => {
  const initializeZoom = async () => {
    await ZoomUs.initialize({
      clientKey: ZOOM_CONFIG.ZOOM_PUBLIC_KEY,
      clientSecret: ZOOM_CONFIG.ZOOM_PRIVATE_KEY,
      domain: 'zoom.us',
    });
  };

  const join = async (name, live) => {
    try {
      await ZoomUs.joinMeeting({
        userName: name,
        meetingNumber: live.meeting_id,
        password: live.password,
        noAudio: true,
        noVideo: true,
        noButtonMore: true,
        noButtonShare: true,
        noTextMeetingId: true,
        noTextPassword: true,
        noInvite: true,
        noShare: true,
      });
    } catch (error) {
      console.log('error', JSON.stringify(error.message));
      // alert(JSON.stringify(error));
      if (error.message === 'joinMeeting, errorCode=101') {
        // alert('Here');
        await ZoomUs.leaveMeeting();
        join(name, live);
      }
    }
    // joinMeeting(name, `${live.meeting_id}`, live.password);
    // joinLive(course, live);
  };

  useEffect(() => {
    initializeZoom();
  }, []);

  return (
    <SafeAreaView style={{flex: 1}}>
      <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
        <TouchableOpacity
          style={{
            height: 60,
            width: 200,
            justifyContent: 'center',
            alignItems: 'center',
            backgroundColor: 'blue',
          }}
          onPress={() => {
            join('Ajmal', {meeting_id: '94152954253', password: '5XIMY2'});
          }}>
          <Text style={{color: 'white'}}>Join</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;

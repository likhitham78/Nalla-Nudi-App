import { Tabs } from 'expo-router';
import { House, Heart } from 'lucide-react-native';
import { StyleSheet, View } from 'react-native';
import { Colors } from '@/constants/Colors';

export default function TabLayout() {
  return (
    <Tabs
      screenOptions={{
        headerShown: false,
        tabBarStyle: styles.tabBar,
        tabBarActiveTintColor: Colors.primary,
        tabBarInactiveTintColor: Colors.textMuted,
        tabBarLabelStyle: styles.tabLabel,
      }}
    >
      <Tabs.Screen
        name="index"
        options={{
          title: 'Home',
          tabBarIcon: ({ color, size, focused }) => (
            <View style={[styles.iconWrap, focused && styles.iconWrapActive]}>
              <House color={focused ? '#fff' : color} size={size - 2} />
            </View>
          ),
        }}
      />
      <Tabs.Screen
        name="mylist"
        options={{
          title: 'My List',
          tabBarIcon: ({ color, size, focused }) => (
            <View style={[styles.iconWrap, focused && styles.iconWrapActive]}>
              <Heart color={focused ? '#fff' : color} size={size - 2} fill={focused ? '#fff' : 'none'} />
            </View>
          ),
        }}
      />
    </Tabs>
  );
}

const styles = StyleSheet.create({
  tabBar: {
    backgroundColor: '#FFFFFF',
    borderTopWidth: 0,
    paddingTop: 8,
    paddingBottom: 12,
    height: 68,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: -4 },
    shadowOpacity: 0.06,
    shadowRadius: 12,
    elevation: 8,
  },
  tabLabel: {
    fontFamily: 'Nunito-SemiBold',
    fontSize: 11,
    marginTop: 2,
  },
  iconWrap: {
    width: 36,
    height: 36,
    borderRadius: 18,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'transparent',
  },
  iconWrapActive: {
    backgroundColor: Colors.primary,
  },
});

import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
} from 'react-native';
import { useRouter } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { LinearGradient } from 'expo-linear-gradient';
import { Heart, Brain, BookOpenCheck, ArrowRight } from 'lucide-react-native';
import { Colors, asGradient } from '@/constants/Colors';
import { useWords } from '@/context/WordContext';
import WordCard from '@/components/WordCard';
import Animated, { FadeInDown } from 'react-native-reanimated';

export default function MyListScreen() {
  const router = useRouter();
  const { savedWords } = useWords();

  return (
    <SafeAreaView style={styles.safe} edges={['top']}>
      {/* Header */}
      <View style={styles.header}>
        <View>
          <Text style={styles.headerTitle}>My List</Text>
          <Text style={styles.headerSub}>{savedWords.length} saved words</Text>
        </View>
        <View style={styles.headerIcon}>
          <Heart color={Colors.error} size={22} fill={Colors.error} />
        </View>
      </View>

      {savedWords.length > 0 && (
        <Animated.View entering={FadeInDown.delay(100)}>
          <TouchableOpacity
            style={styles.flashcardBtn}
            onPress={() => router.push('/flashcards')}
            activeOpacity={0.85}
          >
            <LinearGradient
              colors={asGradient(Colors.gradientSuccess)}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 1 }}
              style={styles.flashcardGradient}
            >
              <Brain color="#fff" size={20} />
              <Text style={styles.flashcardBtnText}>Start Flashcards</Text>
              <ArrowRight color="#fff" size={18} />
            </LinearGradient>
          </TouchableOpacity>
        </Animated.View>
      )}

      <FlatList
        data={savedWords}
        keyExtractor={(item) => item.id}
        renderItem={({ item, index }) => (
          <WordCard word={item} index={index} onPress={() => router.push(`/word/${item.id}`)} />
        )}
        contentContainerStyle={styles.listContent}
        ListEmptyComponent={
          <View style={styles.emptyState}>
            <View style={styles.emptyIconWrap}>
              <BookOpenCheck color={Colors.textMuted} size={48} />
            </View>
            <Text style={styles.emptyTitle}>No saved words yet</Text>
            <Text style={styles.emptySubtitle}>
              Search for words and tap the heart icon to save them here for quick review.
            </Text>
            <TouchableOpacity
              style={styles.exploreBtn}
              onPress={() => router.push('/')}
              activeOpacity={0.85}
            >
              <LinearGradient
                colors={asGradient(Colors.gradientHero)}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 1 }}
                style={styles.exploreGradient}
              >
                <Text style={styles.exploreBtnText}>Explore Words</Text>
                <ArrowRight color="#fff" size={16} />
              </LinearGradient>
            </TouchableOpacity>
          </View>
        }
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: Colors.background },

  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    paddingVertical: 16,
  },
  headerTitle: {
    fontFamily: 'Nunito-ExtraBold',
    fontSize: 28,
    color: Colors.text,
  },
  headerSub: {
    fontFamily: 'Nunito-Regular',
    fontSize: 14,
    color: Colors.textMuted,
    marginTop: 3,
  },
  headerIcon: {
    width: 48,
    height: 48,
    borderRadius: 16,
    backgroundColor: '#FEE2E2',
    alignItems: 'center',
    justifyContent: 'center',
  },

  flashcardBtn: {
    marginHorizontal: 20,
    marginBottom: 16,
    borderRadius: 16,
    overflow: 'hidden',
    shadowColor: Colors.success,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 12,
    elevation: 6,
  },
  flashcardGradient: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
    paddingVertical: 16,
    justifyContent: 'center',
  },
  flashcardBtnText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 16,
    color: '#fff',
  },

  listContent: {
    paddingHorizontal: 20,
    paddingBottom: 32,
  },

  emptyState: {
    alignItems: 'center',
    marginTop: 56,
    paddingHorizontal: 32,
    gap: 14,
  },
  emptyIconWrap: {
    width: 96,
    height: 96,
    borderRadius: 48,
    backgroundColor: Colors.surfaceAlt,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 8,
  },
  emptyTitle: {
    fontFamily: 'Nunito-Bold',
    fontSize: 22,
    color: Colors.text,
  },
  emptySubtitle: {
    fontFamily: 'Nunito-Regular',
    fontSize: 15,
    color: Colors.textMuted,
    textAlign: 'center',
    lineHeight: 24,
  },
  exploreBtn: {
    marginTop: 8,
    borderRadius: 14,
    overflow: 'hidden',
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 4,
  },
  exploreGradient: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    paddingHorizontal: 28,
    paddingVertical: 14,
  },
  exploreBtnText: {
    fontFamily: 'Nunito-Bold',
    fontSize: 15,
    color: '#fff',
  },
});

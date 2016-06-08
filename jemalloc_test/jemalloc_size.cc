#include <iostream>
#include <fstream>
#include <jemalloc/jemalloc.h>

#define zmalloc_size(p) je_malloc_usable_size(p)

using namespace std;

int main(int argc, char **argv)
{
  int init_size = 2, interval_size = 16, total_size = 0;
  int count = 511;
  ofstream os("jemalloc_distrib", ofstream::binary);

  if (!os.is_open()) {
    fprintf(stderr, "file open fail\n");
    return -1;
  }

  total_size += init_size;
  void *newptr = je_malloc(total_size);
  if (NULL == newptr) {
    return -1;
  }
  os << total_size << " " << zmalloc_size(newptr) << endl;

  for (int i = 0; i < count; i++) {
    total_size += interval_size;
    newptr = je_realloc(newptr, total_size);
    if (NULL == newptr) {
      return -1;
    }
    os << total_size << " " << zmalloc_size(newptr) << endl;
  }

  je_free(newptr);
  newptr = NULL;

  if (os.is_open()) {
    os.close();
  }

  return 0;
}
